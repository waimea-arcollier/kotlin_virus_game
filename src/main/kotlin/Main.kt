import java.awt.Cursor
import java.awt.Font
import java.awt.Point
import javax.swing.*


fun ImageIcon.scaled(width: Int, height: Int): ImageIcon =
    ImageIcon(image.getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH))


/**
 * Application entry point
 */
fun main() {
     UIManager.setLookAndFeel("com.jtattoo.plaf.luna.LunaLookAndFeel")   // Initialise the LAF

    val app = App()                 // Get an app state object
    val window = MainWindow(app)    // Spawn the UI, passing in the app state

    SwingUtilities.invokeLater { window.show() }
}

//class Window(
//    val name: String,
//    val bgBefore: String,
//    val bgAfter: String,
//    val targetLocation: Point,
//    val targetWidth: Int,
//    val targetHeight: Int,
//    val cookieImage: String,
//    val cookieLocation: Point
//){
//    val bgBeforeIcon: ImageIcon
//    val bgAfterIcon: ImageIcon
//    val cookieImageIcon: ImageIcon
//    var cookieCollected: Boolean = false
//
//    init {
//        bgBeforeIcon = ImageIcon(ClassLoader.getSystemResource("images/" + bgBefore))
//        bgAfterIcon = ImageIcon(ClassLoader.getSystemResource("images/" + bgAfter))
//        cookieImageIcon = ImageIcon(ClassLoader.getSystemResource("images/" + cookieImage))
//    }
//}
/**
 * Manage app state
 *
 * @property name the user's name
 * @property score the points earned
 */
class App {
    var name = "Test"
    var score = 0

//    val solitaireWindow: Window
//    val minesweeperWindow: Window

    init {
//        solitaireWindow = Window(
//            "solitaire",
//            "solitaire-before",
//            "solitaire-after",
//            Point(30, 30),
//            30,
//            60,
//            "solitaire-cookie",
//            Point(30,30)
//        )
//        minesweeperWindow = Window(
//            "minesweeper",
//            "minesweeper-before",
//            "minesweeper-after",
//            Point(30, 30),
//            30,
//            60,
//            "minesweeper-cookie",
//            Point(30,30)
//        )

    }

}


/**
 * Main UI window, handles user clicks, etc.
 *
 * @param app the app state object
 */
class MainWindow(val app: App) {
    val frame = JFrame("WINDOWS XP HOME")
    val solitaireIcon = ImageIcon(ClassLoader.getSystemResource("images/solitaire-icon.png")).scaled(80, 80)
    private val panel = JPanel().apply { layout = null }

    private var bgLabel = JLabel()

    private val solitaireButton = JButton("Solitaire", solitaireIcon)

    private val solitaireWindow = SolitaireWindow(this, app)      // Pass app state to dialog too

    init {
        setupWindow()
        setupLayout()
        setupStyles()
        setupActions()
        updateUI()
    }

    private fun setupLayout() {
        val bgIcon = ImageIcon(ClassLoader.getSystemResource("images/main-bg.png")).scaled(frame.width, frame.height)
        panel.preferredSize = java.awt.Dimension(400, 1000)
        bgLabel = JLabel(bgIcon)

        bgLabel.setBounds(0, 0, frame.width, frame.height)

        solitaireButton.setBounds(10, 300, 80, 100)
        solitaireButton.cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
//        (10, 30), (10, 190), (10,350), (10,510), (10, 670)

        panel.add(bgLabel)
        bgLabel.add(solitaireButton)
    }

    private fun setupStyles() {

        solitaireButton.verticalTextPosition = SwingConstants.BOTTOM
        solitaireButton.horizontalTextPosition = SwingConstants.CENTER
        solitaireButton.isBorderPainted = false
        solitaireButton.isFocusPainted = false
        solitaireButton.isContentAreaFilled = false

        bgLabel.isVisible = true
    }

    private fun setupWindow() {
        frame.isResizable = false                           // Can't resize
        frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE  // Exit upon window close
        frame.contentPane = panel                           // Define the main content\
        frame.pack()
        val screenSize = java.awt.Toolkit.getDefaultToolkit().screenSize
        frame.setSize(screenSize.width, screenSize.height)
    }

    private fun setupActions() {
        solitaireButton.addActionListener { handleIconClick() }
    }


    private fun handleIconClick() {
        solitaireWindow.show()
    }

    fun updateUI() {
        solitaireWindow.updateUI()       // Keep child dialog window UI up-to-date too
    }

    fun show() {
        frame.isVisible = true
    }
}


/**
 * Info UI window is a child dialog and shows how the
 * app state can be shown / updated from multiple places
 *
 * @param owner the parent frame, used to position and layer the dialog correctly
 * @param app the app state object
 */
class SolitaireWindow(val owner: MainWindow, val app: App) {
    private val dialog = JDialog(owner.frame, "Solitaire", false)
    private val panel = JPanel().apply { layout = null }

    private val backLabel = JLabel()
    private val targetButton = JButton()

    val cookieLocation: Point

    val bgBeforeIcon: ImageIcon
    val bgAfterIcon: ImageIcon
    val cookieImageIcon: ImageIcon
    var cookieCollected: Boolean = false

    init {
        bgBeforeIcon = ImageIcon(ClassLoader.getSystemResource("images/solitaire-before.png")).scaled(600,450)
        bgAfterIcon = ImageIcon(ClassLoader.getSystemResource("images/solitaire-after.png")).scaled(600,450)
        cookieImageIcon = ImageIcon(ClassLoader.getSystemResource("images/notes-icon.png"))

        cookieLocation = Point(30, 30)

        setupLayout()
        setupStyles()
        setupActions()
        setupWindow()
        updateUI()
    }

    private fun setupLayout() {
        panel.preferredSize = java.awt.Dimension(600,450)

        backLabel.setBounds(0, 0, 600, 450)
        backLabel.icon = bgBeforeIcon

        targetButton.setBounds(20, 10, 70, 100)
        targetButton.cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)

        panel.add(targetButton)
        panel.add(backLabel)
    }

    private fun setupStyles() {
        targetButton.isBorderPainted = false
        targetButton.isFocusPainted = false
        targetButton.isContentAreaFilled = false
    }

    private fun setupWindow() {
        dialog.isResizable = false                              // Can't resize
        dialog.isAlwaysOnTop = true
        dialog.defaultCloseOperation = JDialog.HIDE_ON_CLOSE    // Hide upon window close
        dialog.contentPane = panel // Main content panel
        dialog.setLocationRelativeTo(null)
        dialog.pack()
    }

    private fun setupActions() {
        targetButton.addActionListener {handleCardClick()}
    }

    private fun handleCardClick() { // Update the app state
        targetButton.isEnabled = false
        backLabel.icon = bgAfterIcon

//      cookieButton.isVisible = true
    }

    fun updateUI() {

    }

    fun show() {
        dialog.isVisible = true
    }
}