package com.adniyo.checkam.utilities;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
public class Bet9jaConnectServImp implements Bet9jaConnectServ {

    @Override
    public JSONArray competitionList(long gameId) {
        try {
            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, "{\"IDEvento\":" + gameId
                    + ",\"IDGruppoQuota\":-1,\"TipoVisualizzazione\":1,\"DataInizio\":637303680000000000,\"DataFine\":637309728000000000}");
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

            return availableGameList;

        } catch (Exception e) {
            // TODO: handle exception
        }
        return null;
    }

}