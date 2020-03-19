package com.jadyn.ai.ushow

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.jadyn.ai.medialearn.R
import com.jadyn.ai.presenter.ushow.UShowViewModel

/**
 *@version:
 *@FileDescription:
 *@Author:Jing
 *@Since:2019-09-05
 *@ChangeList:
 */
class UShowActivity : AppCompatActivity() {

    private lateinit var viewModel: UShowViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ushow)
        viewModel = ViewModelProvider(this).get(UShowViewModel::class.java)
    }
}