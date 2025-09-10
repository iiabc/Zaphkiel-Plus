package ink.ptms.zaphkiel.impl.feature.kether

import com.notkamui.keval.Keval
import taboolib.module.kether.*

/**
 * keval "1 + 2 * 3"
 * keval "sqrt(16) + sin(30)"
 */
@KetherParser(["keval"], namespace = "zaphkiel-mapping", shared = true)
private fun parserKeval() = scriptParser {
    val expression = it.nextParsedAction()
    actionTake {
        run(expression).str { expr ->
            try {
                Keval.eval(expr)
            } catch (e: Exception) {
                error("Keval calculation error: ${e.message}")
            }
        }
    }
}