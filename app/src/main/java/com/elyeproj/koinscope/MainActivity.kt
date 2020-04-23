package com.elyeproj.koinscope

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.get
import org.koin.android.ext.android.getKoin
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.named
import org.koin.core.scope.Scope
import org.koin.core.scope.ScopeID
import org.koin.ext.scope

class MainActivity : AppCompatActivity() {

    private lateinit var container: Container
    private lateinit var typeQualifiedScope: Scope
    private lateinit var stringQualifiedScope: Scope

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        container = get()
        typeQualifiedScope = container.scope
        stringQualifiedScope = getKoin().createScope("SomeName", named("AScopeName"))

        linkScope()
        btn_get_variable.setOnClickListener {
            getValue()
        }
        btn_name_scope.setOnClickListener {
            stringQualifiedScope.close()
            stringQualifiedScope = getKoin().createScope("SomeName", named("AScopeName"))
            linkScope()
            getValue()
        }
        btn_container_scope.setOnClickListener {
            typeQualifiedScope.close()
            container = get()
            typeQualifiedScope = container.scope
            linkScope()
            getValue()
        }

        txt_single.setOnClickListener {
            createEnvironment(getKoin()._scopeRegistry.rootScope.id, "single")
        }
        txt_factory.setOnClickListener {
            createEnvironment(getKoin()._scopeRegistry.rootScope.id, "factory")
        }
        txt_named_scope.setOnClickListener {
            createEnvironment(stringQualifiedScope.id, "scopedName")
        }
        txt_named_factory.setOnClickListener {
            createEnvironment(stringQualifiedScope.id, "factoryName")
        }
        txt_object_scope.setOnClickListener {
            createEnvironment(typeQualifiedScope.id, "scopedContainer")
        }
        txt_object_factory.setOnClickListener {
            createEnvironment(typeQualifiedScope.id, "factoryContainer")
        }
        txt_object_link_name_scope.setOnClickListener {
            createEnvironment(typeQualifiedScope.id, "scopedName")
        }
        txt_name_link_object_scope.setOnClickListener {
            createEnvironment(stringQualifiedScope.id, "scopedContainer")
        }
    }

    fun createEnvironment(scopeID: ScopeID, qualifierName: String) {
        val environment = get<Environment>(parameters = { parametersOf(scopeID, qualifierName) })
        txt_environment_dependency.text = environment.dependency.toString()
    }

    private fun linkScope() {
        typeQualifiedScope.linkTo(stringQualifiedScope)
        stringQualifiedScope.linkTo(typeQualifiedScope)
    }

    private fun getValue() {
        txt_single.text = get<Dependency>(qualifier = named("single")).toString()
        txt_factory.text = get<Dependency>(qualifier = named("factory")).toString()
        txt_named_scope.text =
            stringQualifiedScope.get<Dependency>(qualifier = named("scopedName")).toString()
        txt_named_factory.text =
            stringQualifiedScope.get<Dependency>(qualifier = named("factoryName")).toString()
        txt_object_scope.text =
            typeQualifiedScope.get<Dependency>(qualifier = named("scopedContainer")).toString()
        txt_object_factory.text =
            typeQualifiedScope.get<Dependency>(qualifier = named("factoryContainer")).toString()
        txt_object_link_name_scope.text =
            typeQualifiedScope.get<Dependency>(qualifier = named("scopedName")).toString()
        txt_name_link_object_scope.text =
            stringQualifiedScope.get<Dependency>(qualifier = named("scopedContainer")).toString()
    }

    override fun onDestroy() {
        super.onDestroy()
        typeQualifiedScope.close()
        stringQualifiedScope.close()
    }
}
