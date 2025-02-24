package com.example.myapplication.Activities;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.media3.common.MediaItem;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView;

import com.example.myapplication.R;

public class NotificationActivity extends AppCompatActivity {
    TextView textView;
    PlayerView playerView;

    private static final String VIDEO_MED =
            "https://ffabe44425110517552e5e641b4af9c59b9bbb58cb667df96c5d419-apidata.googleusercontent.com/download/storage/v1/b/bcitdataforclass/o/Med.mp4?jk=AVyuY3hNBKiauBfEur1eRckb1n5xby9QaUV5GoUk67TnXxdqKnjIZwr7VYKdoacUh47WZ3LNZw_H12N_Ctu3eovDKckEGm_-kFQB6UAi10dbuvd8ZY9wMtI3dbfnUfRb86pvS5zWjd7cezO2KQ0ST-fPnBmgJXtaA5aBDXBtRUqBk-aZFi44t_bUq1w1FRU7krAAcZNmBz3DcpIbpkulVjxVUrjVft5hF_XnB_lFa3rXYY_GNAg5IYu0KnWd3-ZXSD0DZbeOJj8rIS_Bu0ijBpTRTfYNqpKMRS9rKzJHB9-yZeoiNrBsjpD76X-RGevqHGJKcIy925Q2uAsLe4cOAGGM4qIVCNZgsoFvIff0k1JujsFy1WYkXKngm_3006Ug5IAs46bU-oAfRuvsTtZSsisx4LE98iHgQmEVXTsCiUoDTG9aj_M_GGrEbzSzQ8GdWUep0z8dzgk7zLHYf2CTI5IjA0bFnipeFhtSSHPWFg5S-75Sqgl6UrREfAF8G6QEcvWyNeh2hYVcoccKFkyWAuTqxJObCWNrOSCmkeAVLCbzp7XT0CU3b6B3ldA8rHYjk44tPc8-fcEw_4s7lt7bhUcaBzdmEx5zDF6AXAHRBCJhhs3F9Yv5nKlVe_tnfkR0dBcRy1KF_TV8EtBCQO0KDIuEjL08Kfm1Vxxn_wv_d1I-sLzGC091YdReRPZhURAdsnxLycbikEFvehFMFMOQZpVNsg5HdpS4FQh7ryYLDfsnpP7iLJP_8SriRJpac43qicgcnWQchNBXxtHpUQ3IVo6KVjLImNhHcElxaSsfAFq_sewFNJwdXW99rHwSIRnmWqg2bBodj5ixvuNnX-qV53VPDRb37936JY7LEnpSXSrBq2yUbzeD6NqWddneAs2_uBcOcXYVdK6XwKGJEC9mvsv2DkRthCNIHY16Tf1gKE8Ej-KBqq08RDtLbQrUIGAzW6OlIALRBjgd8JjTxrFx_ZIFPiqgtb5PKK8e3z8uthxFgiZwV5cZBImAEXdahp9B2IQwQZxC7yGnaOw-1iGUz3isBSLY8A4_HcbA3c5ar4BYItQOVn1q9Buu9DYQZtYJgwuyykhrzRPBWmgD2g_NJLHXSreNqXH6kGiUfy2tlqFj_1MLW2viFlXKHISXzB7BY0e93IuBix8KU1wXjGR-3qccImpxDnmg7sbkuFds4F8V9BlFqMllKkGyLgybimrhT2wM8wlgw0i87Ydl-kxPOpuqVd2oFCZ3o0pQrE_m-OdV7yW9eBJC64gblmek2OU&isca=1";
    private static final String VIDEO_VITAL =
            "https://ffd40e33e68e56a212aa5fc351812b68ef3015cae9d301385b35e76-apidata.googleusercontent.com/download/storage/v1/b/bcitdataforclass/o/Vitals.mp4?jk=AVyuY3jJpaqv05sOnnFbbffAoHlN2DDix8TL8lDtOLi4S-lFePEosACbdLHcwI7Tq8kP8mVCxhNExzEIPe-C2KqJ7Nx_-1hNQ7FQktYvPIKYUxtNpq2eNDCkNtQ3KerjbVChJhaMgo9eEMXZzUSJ9Z_PmWJqSeB25zeh2qXpayVMQAFPmgQfB0_HjAvS5_xUuvQJJgN5U-cRJ1aWgN_OFF_js8RqUEShAKvj7Bn7q3oaKMesX1olzOu2ToryitIKInTS3IRgXL6jXoFvqd82J-G5ook940HIFpZg2Sppv6FpAAl_zNK2ZnL95vJRuQ45wWxqZXVoLt2lbfwCzLkofg1m-Wr08RusaOaxWeGJnl_bnmgmPXWeQmJwee_xpvpODEhDttC6RLCLtKSMZVtsCUjJa-cE1FzsnQYYAVruYQ67TJ6OwNBM7_9-ovznuMs4fhpqc2JztMjZS0AzKUTmVY14dmGEL4IjoAyuwylA16aWrp5HkaNLT2aKhJLo8pKjEZ2uIzkWo8DhAYT_K-vLOZomwMP5oM3QKw7WclZ8IDoBvGQ4nK8zRmlsN8-kp1W-DBxg5XrZ8IR1r2ptBuzyM6YMCkVUYB7-vz1kPizTliSNRE-SEyjnaCm6FMxqukP4bM3CAHaYHY0asXwCNQBQCvgATAPNf01aHF00KDBuxH5cdVUfZ6YQeiPWYT-xAW3fFbeld18YAGiqL0LxXFIj-6U33_tx8pBcLhqakDCtCAypzarAd_iyVi0tVVjOjL4dSWwNITSV0TfQtcUu3sGqk3hMSnW1WetXtzAPYr8Do5NdJ2n-O-2RkagLr36QREq46z5jroG8F6AyWo5yhtiNEY40fhUlu0NqLS-gnf3dMsiWQUa24us-sU4jMccCpkuJxV_JdI19td4HdLy-CkyHZQ_6G3zKGlV8n3ovy9hGWLZarcSUH8PZlZ3fXyZXUoGpoyZv1OjmcpmN6vESKsU_sW0q_m9pfL2kg-M8HCkUTJA_puncR8kmDc0rKS6sZwnor3nL2GdkC0PgI9blsbtNLa0m_s3pbd-1sax0xZ8CQ-Bhajs2ORkvEnAS0JCwGzb5uADD6sIj54VskeuasA3lBILkvhCxdcuIv82BmLMx0V6Tk0JQymfcUtXJjt3GbKZjkqWlm4oG6QwV1ZJFkO6dtUFVCL2Jf1-TQxFzsfaMwNWt4uCvhUsLLKt8hbKJY7tdqyDIMyJNLTYGb4dK1bZ5F8-id6eC95LfV7ClwBhMf82ycqjIl5AiF3I5cXM9pw_EuRE&isca=1";
    private static final String VIDEO_HOUSE =
            "https://ff0610840938da55bbf754b9269e25787e94f14dff8064ba8ff3ff1-apidata.googleusercontent.com/download/storage/v1/b/bcitdataforclass/o/House.mp4?jk=AVyuY3gc7LEvikxTE_tpOMCVFJP1g2JJkdZbtJaPinxomzKZO2vomJorVywKEhXFClvO9GGCRjOxBS4hhyKilT_NSgraI8pdnYYoBlj8yQIyaHbCSYXYnJdfX67FlsjqsjEScVguGzxt1Igq6pJLq5loeQ55nKSYnh6WxJaDJ6BwM9VQe7P2BC8qRt8KH9RxQ9g__xCQ1_0cpIGdZ3A1SQIOUZetDgk_1iym9Lg83KjEUa3OfQRyRJExf2Dzv2gXByeC6I3esKtp5HTbMtAM8Nml4sL_U088T_O4oasvt8OFkgYVdokBqkXbYzg_UcgnLWTCOpyGrwV9iQIhX0_PAkPg5RWMhnbgPB0gHEPOW5TmRIkLZdj9cHo1xnmKTMIFUyZlZEwOyDCZbblecUgYmYpQfnbVd8qca0tGEIzwlBy7NOYgx_d8J62ygFhTw6QeYY8xLQBwX-5zPJv3UKEL6FPYE0E3EF8ep0z0sit77md767SAcSy0AYi-iY8ePqqX3-nXTY-0YMGdTOm_FraUHcgLGBasvxxkPihpraprNdhpEun6LQn5X5EWJ_MpFvzG3bTKe7DzrV_DcSXOKJ03uOfVf34u48AZdHVv-A77qTEdKqTnJ7z0sNknauhb0GCbz9ChDIwSp81EfeAn32zh8MrpUr4X93s7byBjK90EkH7ejMiRCtXnxe6R9zn0BDECNJH-wAzsj4QGToDUUaA8fyuxfYTK7okOKpYKCRxXA2i462o26CJW16CZI8rI_dLqKSurHj72Rg2liYTue9LT-Qi181CRxJMKuR9_2a3L8iPxRoS3V-f2mApDlCI5gmBVvwwhaRDD-v-dLQTfmsn8celcfjGNTApf_HO_1iDZ1qLHQzKK792UzEDP61v_41S7tGgigcRwsrFcz-FLjMbp_o_bIfDyL3BRV6LvQOxzAKFI8SARZUPQ1aCgKMyVhK2cMGZmogvRbcMSDP7oUYH8aPeX-0Lo_71FZSaCwS9TzLUdipUYYzzP4srMbe4X-_FL9zY3cEanGqoyyJ-gaaUQJ-4Yg2hXGuT61xg_G6p8QxDgFSA58l6obYW4xqGxIrbXbMzFk6PSSj659SxUx7jK_5gweZGl4tBXIpW54NeX58J4QrCviwbEFOkZjzEbDnpjXV7RMshHOJRrYXKQa6zwjOGqf7VKbG_6Wn33mez0o4x4fA0GOTa17xCcisPhODGjW9AjNYF30JNzvFkF9yRlBiEIgmM4t-5OdH4m06K1ntbzRCzX8SIxrLouD84CgrCU8Q&isca=1";
    private static final String VIDEO_PERSONAL =
            "https://ffb9193b711efe486d114619818b251fce0d199299be3558d1a2364-apidata.googleusercontent.com/download/storage/v1/b/bcitdataforclass/o/Self.mp4?jk=AVyuY3hVBVcKmn6tnr3BxxNcbI5C9zBbUl-RZJBzeR_SQckLiC2Gejy6RH0kfshrYEJlgN5bkEusTabHhh1V_c2glCG-w5bDBTa1JLRRFHttM1HHPsJ-yWzxnsg6znKrn9S3IQb74Et9CBjkr30HzwGf-GHCqKU60AHJOtPwzHOs13mzT7CP4o7_9OMn-Y4LzmbDE6xRF0c3Xdw4DF5A9u8bcxSqGq4WtythaS77eLWemgxU3BN9FV1OkUo-_U8nwd6zB0rwrogzOkGsWWuIvYryq9lUu8soOn1q7xc5fJM-FVdLh5oK553CyL5N5uXtu0OP29P2UT2Z7jPg60E38X7ohljUDnpi7ZbE7XwkoxQBaerlOlQL6i1BpuLC5-vrZ_S0-2ghBBrn5sSC_1AnpawQeSXex7V2CzU5rYOMYzYMSjEJPnF2htlJuwFx7L-bCIa60Y-e2Fw8O5E56EbABmvdKnW9yu729G-unP9ddtjTmftgQVs5sP-UYS4kWDdyX7eC0-K20M3BkgOUZeP41zPyamm3J7Yy7cMgeQQsVHBpp2YRqLVs_ovXMdPV-jLO13znQM8AiF_Uz_ggcnP_mcjGXU8kvO-M5TpQA7LVuq5zaXnFrUxSJRIEHAG0HessRqa1vegFstTg-4vokJ3xyNT9hKfHBT2UW8KFP6jeta4k-llT2au2vEZWeU5xmdvIZnfb86Ty9dVPSJC-QdMHmWsai7n9HVT7_PyRhiIB7tFEyOIbWPzbUDKcBRUVqnpV6keZiv0UlaCom2zwz_SPkUHHp7tRfrT9n74CPVF3hfM6HcAZaGTC8xTq7eRcmzVYEUIIRqP5LR5XGazTDuO3whGnb94ZV9AdmmN2f3mFUGJSwCy_L0Gh61OgvCcxvYupGf3BWNBnPGwwUkGc2hSfxYej6fu1qiiWBrurtOX51M87WbHmXrF5ioMABzzMUx_J6xqeOa4mPzBcWxJpQ3qZSC4ptqfaT_sXhdrDIMkEFPLRmjVtJRg1uGCVTUjgv4LhjVuZ8kOk9V8vfg6fuBYZYy0cUJNzCMPszemtC4zIvcGH8zGTuaEcYp9EqlkgmiDfwz_349bESu6q3RxtslcrjGzqSIWKbFftQkELVDNmedG_Szn8mYqpP8TiLEWGWUG8Z8eCdINoFrjS6cJUiLZe9Nx57IMrPWleOvGZTWzA-_36ILUZUhhIV44KitCZGicR4oE--R3ap6_s4DNgkf7ejU59bcwjFh04MAhQYqElg-Y7_WeEbstOo1GB2s2yLa3y&isca=1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_view);

        textView = findViewById(R.id.textView);
        playerView = findViewById(R.id.playerView);

        // Getting the notification message from the Intent
        String type = getIntent().getStringExtra("type");
        String date = getIntent().getStringExtra("date");
        String time = getIntent().getStringExtra("time");
        String message = type + "\n" + date + "\n" + time;

        // Set the message to the TextView
        textView.setText(message);

        // Setup ExoPlayer to play the video
        ExoPlayer player = new ExoPlayer.Builder(this).build();
        playerView.setPlayer(player);

        if (type.equalsIgnoreCase("Med Reminders"))
        {
            player.setMediaItem(MediaItem.fromUri(VIDEO_MED));
        }
        else if (type.equalsIgnoreCase("Vitals Check"))
        {
            player.setMediaItem(MediaItem.fromUri(VIDEO_VITAL));
        }
        else if (type.equalsIgnoreCase("House Keeping"))
        {
            player.setMediaItem(MediaItem.fromUri(VIDEO_HOUSE));
        }
        else if (type.equalsIgnoreCase("Personal Care"))
        {
            player.setMediaItem(MediaItem.fromUri(VIDEO_PERSONAL));
        }
        player.prepare();
        player.setPlayWhenReady(true);
    }
}
