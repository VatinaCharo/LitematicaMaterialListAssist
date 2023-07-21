import javafx.application.Application
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.ScrollPane
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.stage.FileChooser
import javafx.stage.Stage
import java.io.File
import java.nio.charset.Charset

fun main(args: Array<String>) {
    println("Program arguments: ${args.joinToString()}")
    Application.launch(App::class.java, args.toString())
}

class App : Application() {
    private val name = "Litematica Material List Play"
    private val version = "v1.0.0"
    override fun start(primaryStage: Stage) {
        val rootVBox = VBox().apply {
            padding = Insets(10.0)
            spacing = 10.0
        }
        val loadHBox = HBox().apply {
            spacing = 10.0
        }
        val fileName = Label("")
        val fileSelectBtn = Button("Select CSV File")
        val loadBtn = Button("Load")

        val operationHBox = HBox().apply {
            spacing = 10.0
        }
        val numSortBtn = Button("Sorted by Num")
        var isNumSortUp = false
        val scrollPane = ScrollPane()
        val materialElementViewVBox = VBox().apply {
            padding = Insets(10.0)
            spacing = 10.0
        }
        var materialFile = File(".")
        var materialElementList = listOf<MaterialElement>()
        val materialTitle = HBox().apply {
            padding = Insets(0.0, 10.0, 0.0, 10.0)
            spacing = 10.0
            children.addAll(
                Label("Delete").apply {
                    alignment = Pos.CENTER
                    prefWidth = 100.0
                },
                Label("name").apply {
                    alignment = Pos.CENTER
                    prefWidth = 100.0
                },
                Label("count").apply {
                    alignment = Pos.CENTER
                    prefWidth = 100.0
                },
                Label("stacks + blocks").apply {
                    alignment = Pos.CENTER
                    prefWidth = 100.0
                },
                Label("boxes + stacks + blocks").apply {
                    alignment = Pos.CENTER
                    prefWidth = 200.0
                },
                Label("Finished?").apply {
                    alignment = Pos.CENTER
                    prefWidth = 100.0
                }
            )
        }

        val mainScene = Scene(rootVBox).apply { fill = null }

        rootVBox.children.addAll(loadHBox, operationHBox, scrollPane)
        loadHBox.children.addAll(fileName, fileSelectBtn, loadBtn)
        operationHBox.children.addAll(numSortBtn)
        scrollPane.content = materialElementViewVBox
        materialElementViewVBox.children.add(materialTitle)
        fileSelectBtn.setOnAction {
            materialFile = FileChooser().apply {
                title = "Select Litematica Material File"
                initialDirectory = File(".")
                extensionFilters.add(
                    FileChooser.ExtensionFilter("Litematica Material Files", "*.csv")
                )
            }.showOpenDialog(primaryStage) ?: return@setOnAction
            fileName.text = materialFile.name
        }
        loadBtn.setOnAction {
            // 清空之前的展示数据
            if (materialElementViewVBox.children.size > 1) {
                materialElementViewVBox.children.clear()
                materialElementViewVBox.children.add(materialTitle)
            }
            // 加载csv数据文件
            val txt = materialFile.readLines(Charset.forName("GBK"))
            val content = txt.map { it.replace("\"", "") }
            val headers = content[0].split(",")
            // 简要判断文件合法性
            if (headers[0] == "Item" && content[1] == "Total") return@setOnAction
            // 读取数据并实例化
            materialElementList = content.subList(1, content.size).map {
                val elements = it.split(",")
                MaterialElement(elements[0], elements[1].toInt())
            }
            // 展示数据
            materialElementViewVBox.children.addAll(materialElementList.map { it.listViewElement })
        }
        numSortBtn.setOnAction {
            // 清空之前的展示数据
            if (materialElementViewVBox.children.size > 1) {
                materialElementViewVBox.children.clear()
                materialElementViewVBox.children.add(materialTitle)
            }
            // 默认升序
            materialElementList = materialElementList.sortedBy { it.count }
            // 若之前升序 则反转成降序
            if (isNumSortUp) materialElementList = materialElementList.reversed()
            // 反转升序指示
            isNumSortUp = !isNumSortUp
            // 展示数据
            materialElementViewVBox.children.addAll(materialElementList.map { it.listViewElement })
        }

        primaryStage.apply {
            scene = mainScene
            title = "$name $version"
            width = 900.0
            height = 600.0
        }.show()
    }
}