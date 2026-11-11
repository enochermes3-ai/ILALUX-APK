package com.ilalux.observatorio;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private final List<Animator> bucles = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.cardWeb).setOnClickListener(v ->
            startActivity(new Intent(Intent.ACTION_VIEW,
                Uri.parse("https://www.observatorioilalux.wuaze.com"))));
        findViewById(R.id.cardBoletin).setOnClickListener(v ->
            startActivity(new Intent(this, BoletinActivity.class)));
        findViewById(R.id.cardTemario).setOnClickListener(v ->
            startActivity(new Intent(this, TemarioActivity.class)));

        animarLogo();

        int[] ids = {R.id.tvTitulo, R.id.tvSub, R.id.cardWeb, R.id.cardBoletin,
                     R.id.cardTemario, R.id.cardFoto, R.id.tvFooter};
        for (int i = 0; i < ids.length; i++) aparecer(findViewById(ids[i]), 700 + i * 130L);
    }

    /** Entrada del logo (zoom + rebote) y luego flotacion suave; las estrellas titilan. */
    private void animarLogo() {
        final float d = getResources().getDisplayMetrics().density;
        final View caja = findViewById(R.id.logoBox);
        final View estrellas = findViewById(R.id.logoStars);

        caja.setAlpha(0f);
        caja.setScaleX(0.8f);
        caja.setScaleY(0.8f);
        caja.setTranslationY(26 * d);
        caja.animate().alpha(1f).scaleX(1f).scaleY(1f).translationY(0f)
            .setDuration(950).setInterpolator(new OvershootInterpolator(1.1f))
            .withEndAction(() -> {
                ObjectAnimator flota = ObjectAnimator.ofFloat(caja, View.TRANSLATION_Y, 0f, -6 * d);
                configurarBucle(flota, 2800);
            }).start();

        estrellas.setAlpha(0f);
        estrellas.animate().alpha(1f).setStartDelay(650).setDuration(900)
            .withEndAction(() -> {
                ObjectAnimator titila = ObjectAnimator.ofFloat(estrellas, View.ALPHA, 1f, 0.25f);
                configurarBucle(titila, 1300);
            }).start();
    }

    private void configurarBucle(ObjectAnimator a, long duracion) {
        a.setDuration(duracion);
        a.setRepeatCount(ValueAnimator.INFINITE);
        a.setRepeatMode(ValueAnimator.REVERSE);
        a.setInterpolator(new AccelerateDecelerateInterpolator());
        bucles.add(a);
        a.start();
    }

    private void aparecer(View v, long retraso) {
        float d = getResources().getDisplayMetrics().density;
        v.setAlpha(0f);
        v.setTranslationY(36 * d);
        v.animate().alpha(1f).translationY(0f).setStartDelay(retraso)
            .setDuration(550).setInterpolator(new DecelerateInterpolator()).start();
    }

    @Override
    protected void onDestroy() {
        for (Animator a : bucles) a.cancel();
        bucles.clear();
        super.onDestroy();
    }
}
