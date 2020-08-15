package com.adniyo.checkam.controller;

import org.json.JSONArray;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.adniyo.checkam.entity.bet9ja_gameGroup;
import com.adniyo.checkam.entity.teamList;
import com.adniyo.checkam.service.GameGroupServ;
import com.adniyo.checkam.service.GameListServ;
import com.adniyo.checkam.service.TeamListServ;
import com.adniyo.checkam.utilities.Bet9jaConnectServ;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.adniyo.checkam.entity.bet9ja;

@Controller
public class CustomerController {

	@Autowired
	private GameGroupServ gameGroupServ;
	@Autowired
	private Bet9jaConnectServ bConnect;
	@Autowired
	private TeamListServ addTeam;
	@Autowired
	private GameListServ addGame;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ResponseEntity<String> landing() {

		return new ResponseEntity<String>("We got here!", new HttpHeaders(), HttpStatus.OK);

	}

	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index() {
		return "index";
	}

	@RequestMapping(value = "/bgrouplist", method = RequestMethod.GET)
	public String bgrouplist() {
		try {
			OkHttpClient client = new OkHttpClient();
			MediaType mediaType = MediaType.parse("application/json");
			RequestBody body = RequestBody.create(mediaType,
					"{\"IDPalinsesto\":1,\"IDLingua\":2,\"TipoVisualizzazione\":1,\"StartDate\":637315776000000000,\"EndDate\":637321824000000000}");
			Request request = new Request.Builder().url("https://web.bet9ja.com/Controls/ControlsWS.asmx/GetPalimpsest")
					.method("POST", body).addHeader("Content-Type", "application/json").build();
			Response response = client.newCall(request).execute();
			String resStr = response.body().string();
			JSONObject json = new JSONObject(resStr);

			JSONArray availableGameList = json.getJSONObject("d").optJSONArray("SportList");
			List<bet9ja_gameGroup> availableGroups = new ArrayList<>();
			for (int i = 0; i < availableGameList.length(); i++) {
				System.out.println(availableGameList.getJSONObject(i).optString("Sport"));
				// this could a potential breaking point.. soccer includes space
				if (availableGameList.getJSONObject(i).optString("Sport").equals("Soccer ")) {
					JSONArray competitionBody = availableGameList.getJSONObject(i).optJSONArray("GroupList");
					for (int j = 0; j < competitionBody.length(); j++) {
						String competitionGroupName = competitionBody.getJSONObject(j).optString("Gruppo");
						JSONArray competitionGroup = competitionBody.getJSONObject(j).optJSONArray("EventList");
						for (int k = 0; k < competitionGroup.length(); k++) {
							String competitionName = competitionGroup.getJSONObject(k).optString("Evento");
							long competitionID = Long
									.parseLong(competitionGroup.getJSONObject(k).optString("IDEvento"));
							LocalDateTime dateTime = LocalDateTime.now();
							// System.out.println(competitionGroupName + "|" + competitionName + "|" +
							// competitionID + "|"
							// + dateTime);

							bet9ja_gameGroup competitionList = bet9ja_gameGroup.builder()
									.competitionBody(competitionGroupName).competitionName(competitionName)
									.lastUpdateTime(dateTime).competitionId(competitionID).build();
							if (gameGroupServ.findByGameId(competitionList.getCompetitionId()) == null) {
								availableGroups.add(competitionList);
								// System.out.println(competitionList);
							}

						}

					}
					break;
				}

			}
			if (availableGroups.size() > 0) {
				gameGroupServ.save(availableGroups);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return "index";
	}

	@RequestMapping(value = "/gamelist", method = RequestMethod.GET)
	public String gamelist() {
		// List<teamList> a = addTeam.findBySiteName("FK Suduva
		// Marijampole","bet9jaName");
		List<bet9ja_gameGroup> competitionList = gameGroupServ.findAll();

		// TODO change to a hashTable
		List<teamList> addToMaster = new ArrayList<>();
		List<bet9ja> addToBMaster = new ArrayList<>();
		int so = 0;
		for (bet9ja_gameGroup bet9ja_gameGroup : competitionList) {
			so++;
			if (so == 5) {
				break;
			}
			JSONArray availCompGameList = bConnect.competitionList(bet9ja_gameGroup.getCompetitionId());
			if (availCompGameList != null) {
				for (int i = 0; i < availCompGameList.length(); i++) {
					String matchanme = availCompGameList.getJSONObject(i).optString("SottoEvento");
					String[] matchTeam = matchanme.split(" - ");
					if (matchTeam.length != 2) {
						// TODO: send alert
						System.out.println("=================================");
						System.out.println("THIS IS NOT GOOD :" + matchanme);
						System.out.println("=================================");
					} else {
						// System.out.println(matchanme);
						boolean isFound = true;
						long[] teamId = new long[2];

						for (int j = 0; j < matchTeam.length; j++) {
							matchTeam[j] = matchTeam[j].trim();
							teamList checkTeam = teamList.builder().teamName(matchTeam[j]).bet9jaName(matchTeam[j])
									.build();
							List<teamList> teamCode = addTeam.findBySiteName(matchTeam[j], "bet9jaName");
							if (teamCode.isEmpty() && !addToMaster.contains(checkTeam)) {
								addToMaster.add(checkTeam);
								isFound = false;
								break;

							} else {
								teamId[j] = teamCode.get(0).getTeamListId();
							}
						}
						if (!isFound) {
							continue;
						}
						String time = availCompGameList.getJSONObject(i).optString("StrDataInizio") + " "
								+ availCompGameList.getJSONObject(i).optString("StrOraInizio");
						JSONArray availOddsList = availCompGameList.getJSONObject(i).optJSONArray("Quote");
						//System.out.println(availOddsList.toString());
						//System.out.println("=================================");
						double homeWin, draw, awayWin, homeWinOrDraw, homeWinOrAwayWin, awayWinOrDraw, over2Goals,
								under3Goals;
						long homeWinCode, drawCode, awayWinCode, homeWinOrDrawCode, homeWinOrAwayWinCode,
								awayWinOrDrawCode, over2GoalsCode, under3GoalsCode;
						homeWin = draw = awayWin = homeWinOrDraw = homeWinOrAwayWin = awayWinOrDraw = over2Goals = under3Goals = 0.00;
						homeWinCode = drawCode = awayWinCode = homeWinOrDrawCode = homeWinOrAwayWinCode = awayWinOrDrawCode = over2GoalsCode = under3GoalsCode = 0;
						DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy HH:mm");
						LocalDateTime dateTime = LocalDateTime.parse(time, formatter);
						for (int j = 0; j < availOddsList.length(); j++) {
							double odd = 1.00;
							if (!availOddsList.getJSONObject(j).optString("StrQuotaValore").equals("-") &&
									!availOddsList.getJSONObject(j).optString("IDQuota").equals("0.0")
							 ) {
								//System.out.println(availOddsList.getJSONObject(j).optString("StrQuotaValore"));
								odd = Double.parseDouble(availOddsList.getJSONObject(j).optString("StrQuotaValore"));
							}

							switch (availOddsList.getJSONObject(j).optString("TipoQuota")) {
								case "1":
									homeWin = odd;
									homeWinCode = Long.parseLong(availOddsList.getJSONObject(j).optString("IDQuota"));
									break;
								case "X":
									draw = odd;
									drawCode = Long.parseLong(availOddsList.getJSONObject(j).optString("IDQuota"));
									break;
								case "2":
									awayWin = odd;
									awayWinCode = Long.parseLong(availOddsList.getJSONObject(j).optString("IDQuota"));
									break;
								case "1X":
									homeWinOrDraw = odd;
									homeWinOrDrawCode = Long.parseLong(availOddsList.getJSONObject(j).optString("IDQuota"));
									break;
								case "12":
									homeWinOrAwayWin = odd;
									homeWinOrAwayWinCode = Long.parseLong(availOddsList.getJSONObject(j).optString("IDQuota"));
									break;
								case "X2":
									awayWinOrDraw = odd;
									awayWinOrDrawCode = Long.parseLong(availOddsList.getJSONObject(j).optString("IDQuota"));
									break;
								case "Over":
									over2Goals = odd;
									over2GoalsCode = Long.parseLong(availOddsList.getJSONObject(j).optString("IDQuota"));
									break;
								case "Under":
									under3Goals = odd;
									under3GoalsCode = Long.parseLong(availOddsList.getJSONObject(j).optString("IDQuota"));
									break;
							}
						}
						bet9ja game = bet9ja.builder()
								.bet9jaIndex(availCompGameList.getJSONObject(i).optLong("IDSottoEvento"))
								.awayTeam_id(teamId[1]).homeTeam_id(teamId[0]).matchTime(dateTime).homeWin(homeWin)
								.awayWin(awayWin).draw(draw).homeWinOrDraw(homeWinOrDraw).awayWinOrDraw(awayWinOrDraw)
								.homeWinOrAwayWin(homeWinOrAwayWin).over2Goals(over2Goals).under3Goals(under3Goals)
								.homeWinCode(homeWinCode).awayWinCode(awayWinCode).drawCode(drawCode)
								.homeWinOrDrawCode(homeWinOrDrawCode).awayWinOrDrawCode(awayWinOrDrawCode)
								.homeWinOrAwayWinCode(homeWinOrAwayWinCode).over2GoalsCode(over2GoalsCode)
								.under3GoalsCode(under3GoalsCode).build();

						if (addGame.findById(game.getBet9jaIndex()) == null && !addToBMaster.contains(game)) {
							System.out.println(game);
							addToBMaster.add(game);
						}
						// System.out.println();
						// System.out.println("=============================================");

					}
				}
			}

			// break;
		}
		// System.out.println(addToMaster.size());
		addTeam.addTeamToMaster(addToMaster);
		addGame.save(addToBMaster);
		return "index";
	}

	@RequestMapping(value = "/bet9ja", method = RequestMethod.GET)
	public String getDetail() {
		try {
			OkHttpClient client = new OkHttpClient();
			MediaType mediaType = MediaType.parse("application/json");
			RequestBody body = RequestBody.create(mediaType,
					"{\"IDEvento\":943734,\"IDGruppoQuota\":-1,\"TipoVisualizzazione\":1,\"DataInizio\":637303680000000000,\"DataFine\":637309728000000000}");
			Request request = new Request.Builder()
					.url("https://web.bet9ja.com/Controls/ControlsWS.asmx/OddsViewFullEvent").method("POST", body)
					.addHeader("Content-Type", "application/json")
					.addHeader("Cookie", "ISBetsWebAdmin_CurrentCulture=2; mb9j_nodesession=2030110474.20480.0000")
					.build();
			Response response = client.newCall(request).execute();
			String resStr = response.body().string();
			JSONObject json = new JSONObject(resStr);

			JSONArray availableGameList = json.getJSONObject("d").getJSONObject("Detail")
					.optJSONArray("SottoEventiList");

			// List<bet9ja> listOfavailableGame = new ArrayList<>();
			for (int i = 0; i < availableGameList.length(); i++) {

				int betindex = Integer.parseInt(availableGameList.getJSONObject(i).optString("IDSottoEvento"));
				int homeTeam = 1;
				int awayTeam = 2;
				String time = availableGameList.getJSONObject(i).optString("StrDataInizio") + " "
						+ availableGameList.getJSONObject(i).optString("StrOraInizio");
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy HH:mm");
				LocalDateTime dateTime = LocalDateTime.parse(time, formatter);
				JSONArray odds = availableGameList.getJSONObject(i).optJSONArray("Quote");
				double homeWin, draw, awayWin, homeWinOrDraw, homeWinOrAwayWin, awayWinOrDraw, over2Goals, under3Goals;
				homeWin = draw = awayWin = homeWinOrDraw = homeWinOrAwayWin = awayWinOrDraw = over2Goals = under3Goals = 0.00;

				for (int j = 0; j < odds.length(); j++) {
					switch (odds.getJSONObject(j).optString("TipoQuota")) {
						case "1":
							homeWin = Double.parseDouble(odds.getJSONObject(j).optString("StrQuotaValore"));
							break;
						case "X":
							draw = Double.parseDouble(odds.getJSONObject(j).optString("StrQuotaValore"));
							break;
						case "2":
							awayWin = Double.parseDouble(odds.getJSONObject(j).optString("StrQuotaValore"));
							break;
						case "1X":
							homeWinOrDraw = Double.parseDouble(odds.getJSONObject(j).optString("StrQuotaValore"));
							break;
						case "12":
							homeWinOrAwayWin = Double.parseDouble(odds.getJSONObject(j).optString("StrQuotaValore"));
							break;
						case "X2":
							awayWinOrDraw = Double.parseDouble(odds.getJSONObject(j).optString("StrQuotaValore"));
							break;
						case "Over":
							over2Goals = Double.parseDouble(odds.getJSONObject(j).optString("StrQuotaValore"));
							break;
						case "Under":
							under3Goals = Double.parseDouble(odds.getJSONObject(j).optString("StrQuotaValore"));
							break;
					}

				}

				bet9ja game = bet9ja.builder().bet9jaIndex(betindex).awayTeam_id(awayTeam).homeTeam_id(homeTeam)
						.matchTime(dateTime).homeWin(homeWin).awayWin(awayWin).draw(draw).homeWinOrDraw(homeWinOrDraw)
						.awayWinOrDraw(awayWinOrDraw).homeWinOrAwayWin(homeWinOrAwayWin).over2Goals(over2Goals)
						.under3Goals(under3Goals).build();

				System.out.println(game);

			}

		} catch (Exception e) {
			// TODO: handle exception
		}

		return "index";
	}
}
