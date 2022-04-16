package com.elyeproj.koinscope

import org.koin.core.component.KoinScopeComponent
import org.koin.core.component.createScope
import org.koin.core.scope.Scope

class Container: KoinScopeComponent {
    override val scope: Scope by lazy { createScope(this) }
}