package br.ufpe.cin.if710.rss.activities

import android.app.Activity
import android.os.Bundle
import br.ufpe.cin.if710.rss.R
import br.ufpe.cin.if710.rss.fragments.SettingsFragment

class SettingsActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        // Modificando o nome da ActionBar
        actionBar.title = "Settings"
        // Exibindo o PreferenceFragment
        fragmentManager.beginTransaction()
                .replace(android.R.id.content, SettingsFragment())
                .commit()
    }
}
