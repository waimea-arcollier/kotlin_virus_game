import java.awt.Cursor
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
 * @property
 * @property
 */
class App {
    var cookiesFound = 0

    fun cookieCollected() {
        cookiesFound++

        println("Found a cookie! $cookiesFound found so far")
    }

    fun gameWon(): Boolean {
        return cookiesFound == 5
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
    val minesweeperIcon = ImageIcon(ClassLoader.getSystemResource("images/minesweeper-icon.png")).scaled(80, 80)
    private val panel = JPanel().apply { layout = null }

    private var bgLabel = JLabel()

    private val solitaireButton = JButton("Solitaire", solitaireIcon)
    private val solitaireWindow = SolitaireWindow(this, app)      // Pass app state to dialog too

    private val minesweeperButton = JButton("Minesweeper", minesweeperIcon)
    private val minesweeperWindow = MinesweeperWindow(this, app)


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

        minesweeperButton.setBounds(10, 190, 80, 100)
        minesweeperButton.cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
//        (10, 30), (10, 190), (10,350), (10,510), (10, 670)

        panel.add(bgLabel)
        bgLabel.add(solitaireButton)
        bgLabel.add(minesweeperButton)
    }

    private fun setupStyles() {

        solitaireButton.verticalTextPosition = SwingConstants.BOTTOM
        solitaireButton.horizontalTextPosition = SwingConstants.CENTER
        solitaireButton.isBorderPainted = false
        solitaireButton.isFocusPainted = false
        solitaireButton.isContentAreaFilled = false

        minesweeperButton.verticalTextPosition = SwingConstants.BOTTOM
        minesweeperButton.horizontalTextPosition = SwingConstants.CENTER
        minesweeperButton.isBorderPainted = false
        minesweeperButton.isFocusPainted = false
        minesweeperButton.isContentAreaFilled = false

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
        solitaireButton.addActionListener { handleSolClick() }
        minesweeperButton.addActionListener { handleMineClick() }
    }


    private fun handleSolClick() {
        solitaireWindow.dialog.setLocation(400,200)
        solitaireWindow.show()
    }

    private fun handleMineClick() {
        minesweeperWindow.dialog.setLocation(1200,200)
        minesweeperWindow.show()
    }

    fun updateUI() {
        solitaireWindow.updateUI() // Keep child dialog window UI up-to-date too
        minesweeperWindow.updateUI()
    }

    fun show() {
        frame.isVisible = true
    }

    fun checkIfGameWon() {
        if (app.gameWon()) {
            solitaireWindow.hide()
            minesweeperWindow.hide()
            JOptionPane.showMessageDialog(frame, "Game Won", "Game Over!",  JOptionPane.INFORMATION_MESSAGE)
        }
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
    val dialog = JDialog(owner.frame, "Solitaire", false)
    private val panel = JPanel().apply { layout = null }

    private val backLabel = JLabel()
    private val targetButton = JButton()
    private val cookieButton = JButton()

    val bgBeforeIcon: ImageIcon
    val bgAfterIcon: ImageIcon
    val cookieImageIcon: ImageIcon

    init {
        bgBeforeIcon = ImageIcon(ClassLoader.getSystemResource("images/solitaire-before.png")).scaled(600,450)
        bgAfterIcon = ImageIcon(ClassLoader.getSystemResource("images/solitaire-after.png")).scaled(600,450)
        cookieImageIcon = ImageIcon(ClassLoader.getSystemResource("images/cookie-4.png")).scaled(40, 40)

        setupLayout()
        setupStyles()
        setupActions()
        setupWindow()
        updateUI()
    }

    fun hide() {
        dialog.isVisible = false
    }

    private fun setupLayout() {
        panel.preferredSize = java.awt.Dimension(600,450)

        backLabel.setBounds(0, 0, 600, 450)
        backLabel.icon = bgBeforeIcon

        targetButton.setBounds(20, 10, 70, 100)
        targetButton.cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)

        cookieButton.setBounds(30, 40, 40, 40)
        cookieButton.cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
        cookieButton.icon = cookieImageIcon

        panel.add(cookieButton)
        panel.add(targetButton)
        panel.add(backLabel)
    }

    private fun setupStyles() {
        targetButton.isBorderPainted = false
        targetButton.isFocusPainted = false
        targetButton.isContentAreaFilled = false

        cookieButton.isBorderPainted = false
        cookieButton.isFocusPainted = false
        cookieButton.isContentAreaFilled = false
        cookieButton.isVisible = false
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

        cookieButton.addActionListener {handleCookieClick()}
    }

    private fun handleCardClick() {
        targetButton.isEnabled = false
        backLabel.icon = bgAfterIcon

        cookieButton.isVisible = true
    }

    private fun handleCookieClick() {
        cookieButton.isEnabled = false
        cookieButton.isVisible = false


        app.cookieCollected()
        owner.checkIfGameWon()
    }

    fun updateUI() {

    }

    fun show() {
        dialog.isVisible = true
    }
}

/**
 * Info UI window is a child dialog and shows how the
 * app state can be shown / updated from multiple places
 *
 * @param owner the parent frame, used to position and layer the dialog correctly
 * @param app the app state object
 */
class MinesweeperWindow(val owner: MainWindow, val app: App) {
    val dialog = JDialog(owner.frame, "Minesweeper", false)
    private val panel = JPanel().apply { layout = null }

    private val backLabel = JLabel()
    private val targetButton = JButton()
    private val cookieButton = JButton()

    val bgBeforeIcon: ImageIcon
    val bgAfterIcon: ImageIcon
    val cookieImageIcon: ImageIcon

    init {
        bgBeforeIcon = ImageIcon(ClassLoader.getSystemResource("images/minesweeper-before.png")).scaled(300,350)
        bgAfterIcon = ImageIcon(ClassLoader.getSystemResource("images/minesweeper-after.png")).scaled(300,350)
        cookieImageIcon = ImageIcon(ClassLoader.getSystemResource("images/cookie-5.png")).scaled(125, 125)

        setupLayout()
        setupStyles()
        setupActions()
        setupWindow()
        updateUI()
    }

    fun hide() {
        dialog.isVisible = false
    }

    private fun setupLayout() {
        panel.preferredSize = java.awt.Dimension(300,350)

        backLabel.setBounds(0, 0, 300, 350)
        backLabel.icon = bgBeforeIcon

        targetButton.setBounds(50, 100, 200, 200)
        targetButton.cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)

        cookieButton.setBounds(85, 135, 125, 125)
        cookieButton.cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
        cookieButton.icon = cookieImageIcon

        panel.add(cookieButton)
        panel.add(targetButton)
        panel.add(backLabel)
    }

    private fun setupStyles() {
        targetButton.isBorderPainted = false
        targetButton.isFocusPainted = false
        targetButton.isContentAreaFilled = false

        cookieButton.isBorderPainted = false
        cookieButton.isFocusPainted = false
        cookieButton.isContentAreaFilled = false
        cookieButton.isVisible = false
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

        cookieButton.addActionListener {handleCookieClick()}
    }

    private fun handleCardClick() {
        targetButton.isEnabled = false
        backLabel.icon = bgAfterIcon

        cookieButton.isVisible = true
    }

    private fun handleCookieClick() {
        cookieButton.isEnabled = false
        cookieButton.isVisible = false


        app.cookieCollected()
        owner.checkIfGameWon()
    }

    fun updateUI() {

    }

    fun show() {
        dialog.isVisible = true
    }
}

/**
 * Info UI window is a child dialog and shows how the
 * app state can be shown / updated from multiple places
 *
 * @param owner the parent frame, used to position and layer the dialog correctly
 * @param app the app state object
 */
class MinesweeperWindow(val owner: MainWindow, val app: App) {
    val dialog = JDialog(owner.frame, "Minesweeper", false)
    private val panel = JPanel().apply { layout = null }

    private val backLabel = JLabel()
    private val targetButton = JButton()
    private val cookieButton = JButton()

    val bgBeforeIcon: ImageIcon
    val bgAfterIcon: ImageIcon
    val cookieImageIcon: ImageIcon

    init {
        bgBeforeIcon = ImageIcon(ClassLoader.getSystemResource("images/minesweeper-before.png")).scaled(300,350)
        bgAfterIcon = ImageIcon(ClassLoader.getSystemResource("images/minesweeper-after.png")).scaled(300,350)
        cookieImageIcon = ImageIcon(ClassLoader.getSystemResource("images/cookie-5.png")).scaled(125, 125)

        setupLayout()
        setupStyles()
        setupActions()
        setupWindow()
        updateUI()
    }

    fun hide() {
        dialog.isVisible = false
    }

    private fun setupLayout() {
        panel.preferredSize = java.awt.Dimension(300,350)

        backLabel.setBounds(0, 0, 300, 350)
        backLabel.icon = bgBeforeIcon

        targetButton.setBounds(50, 100, 200, 200)
        targetButton.cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)

        cookieButton.setBounds(85, 135, 125, 125)
        cookieButton.cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
        cookieButton.icon = cookieImageIcon

        panel.add(cookieButton)
        panel.add(targetButton)
        panel.add(backLabel)
    }

    private fun setupStyles() {
        targetButton.isBorderPainted = false
        targetButton.isFocusPainted = false
        targetButton.isContentAreaFilled = false

        cookieButton.isBorderPainted = false
        cookieButton.isFocusPainted = false
        cookieButton.isContentAreaFilled = false
        cookieButton.isVisible = false
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

        cookieButton.addActionListener {handleCookieClick()}
    }

    private fun handleCardClick() {
        targetButton.isEnabled = false
        backLabel.icon = bgAfterIcon

        cookieButton.isVisible = true
    }

    private fun handleCookieClick() {
        cookieButton.isEnabled = false
        cookieButton.isVisible = false


        app.cookieCollected()
        owner.checkIfGameWon()
    }

    fun updateUI() {

    }

    fun show() {
        dialog.isVisible = true
    }
}