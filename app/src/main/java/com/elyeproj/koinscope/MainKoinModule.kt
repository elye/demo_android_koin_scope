package com.elyeproj.koinscope

import org.koin.core.qualifier.named
import org.koin.core.scope.ScopeID
import org.koin.dsl.module

val mainKoinModule =
    module {
        factory { Container() }

        single(qualifier = named("single")) { Dependency() }
        factory(qualifier = named("factory")) { Dependency() }

        scope(named("AScopeName")) {
            scoped(qualifier = named("scopedName")) { Dependency() }
            factory(qualifier = named("factoryName")) { Dependency() }
        }

        scope<Container> {
            scoped(qualifier = named("scopedContainer")) { Dependency() }
            factory(qualifier = named("factoryContainer")) { Dependency() }
        }

        scope<MainActivity> {
            scoped(qualifier = named("scopedActivity")) { Dependency() }
            factory(qualifier = named("factoryActivity")) { Dependency() }
        }

        factory { (scopeId: ScopeID, name: String) ->
            Environment(getScope(scopeId).get(qualifier = named(name)))
        }
    }