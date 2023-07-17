import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.layout.*
import javafx.scene.paint.Color

data class MaterialElement(
    val name: String,
    val count: Int
) {
    private val stackCount = count.floorDiv(64)
    private val stackRemainder = count.mod(64)
    private val boxCount = count.floorDiv(64 * 27)
    private val boxRemainder = count.mod(64 * 27)
    private val boxRemainderStackCount = boxRemainder.floorDiv(64)
    private val delBtn = Button("×").apply { prefWidth = 100.0 }
    private val finishBtn = Button("").apply { prefWidth = 100.0 }
    private var isFinished = false
    val listViewElement = HBox().apply {
        padding = Insets(0.0,10.0,0.0,10.0)
        spacing = 10.0
        background = Background(
            BackgroundFill(
                Color.WHITE,
                CornerRadii.EMPTY,
                Insets.EMPTY
            )
        )
    }

    init {
        listViewElement.children.addAll(
            delBtn,
            Label(name).apply {
                alignment = Pos.CENTER
                prefWidth = 100.0
            },
            Label("$count").apply {
                alignment = Pos.CENTER
                prefWidth = 100.0
            },
            Label("$stackCount + $stackRemainder").apply {
                alignment = Pos.CENTER
                prefWidth = 100.0
            },
            Label("$boxCount + $boxRemainderStackCount + $stackRemainder").apply {
                alignment = Pos.CENTER
                prefWidth = 200.0
            },
            finishBtn
        )
        finishBtn.setOnAction {
            isFinished = !isFinished
            listViewElement.background = Background(
                BackgroundFill(
                    if (isFinished) Color.GRAY else Color.WHITE,
                    CornerRadii.EMPTY,
                    Insets.EMPTY
                )
            )
        }
        delBtn.setOnAction {
            val hbox = (it.source as Button).parent as HBox
            val vbox = hbox.parent as VBox
            vbox.children.remove(hbox)
        }
    }
}