package br.ufpe.cin.if710.rss.fragments

import android.os.Bundle
import android.preference.PreferenceFragment
import br.ufpe.cin.if710.rss.R

class SettingsFragment : PreferenceFragment() {

    // Ao criar o fragmento, nós inflamos o layout que contém a lista de preferências
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.preferencias)
    }
}