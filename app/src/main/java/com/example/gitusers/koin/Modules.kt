package com.example.gitusers.koin

import com.example.gitusers.viewmodels.UsersViewModel
import com.example.gitusers.domain.usecase.FetchUsersUseCase
import com.example.gitusers.network.ApiServiceFactory
import com.example.gitusers.repository.UsersRepository
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object Modules {

    private var viewModels = module {
        viewModel { UsersViewModel(get()) }
    }

    private var useCaseModules = module {
        single { FetchUsersUseCase(get()) }
    }

    private var repoModules = module {
        single { UsersRepository(get()) }

    }

    private var remoteModules = module {
        single { ApiServiceFactory.getApiInterface() }
    }

    fun getAll() = listOf(
        viewModels,
        useCaseModules,
        repoModules,
        remoteModules
    )

}
