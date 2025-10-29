package ink.ptms.zaphkiel.impl.feature.kether

import taboolib.common.env.RuntimeDependencies
import taboolib.common.env.RuntimeDependency
import taboolib.common5.scriptEngine
import taboolib.module.kether.*
import javax.script.SimpleBindings

@RuntimeDependencies(
    RuntimeDependency(
        "!org.openjdk.nashorn:nashorn-core:15.4",
        test = "!jdk.nashorn.api.scripting.NashornScriptEngineFactory"
    )
)
object ActionMath {

    /**
     * zmath "1 + 2 * 3"
     * zmath "sqrt(16) + sin(30)"
     */
    @KetherParser(["zmath", "zap-math"], namespace = "zaphkiel-mapping", shared = true)
    private fun parser() = scriptParser {
        val expression = it.nextParsedAction()
        actionTake {
            run(expression).str { expr ->
                try {
                    calculateExpression(expr, variables().toMap())
                } catch (e: Exception) {
                    error("zap-math calculation error: ${e.message}")
                }
            }
        }
    }

    fun calculateExpression(formula: String, variables: Map<String, Any>): Any? {
        // 创建变量上下文
        val bindings = SimpleBindings(variables)

        // 执行表达式
        return scriptEngine.eval(formula, bindings)
    }

}