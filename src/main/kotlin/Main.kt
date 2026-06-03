/**
 * =====================================================================
 * Programming Project for NCEA Level 3, Standard 91906
 * ---------------------------------------------------------------------
 * Project Name:   Virus Game
 * Project Author: Amber Collier
 * GitHub Repo:    https://github.com/waimea-arcollier/kotlin_virus_game
 * ---------------------------------------------------------------------
 * Notes:
 * PROJECT NOTES HERE
 * =====================================================================
 */


import java.awt.Cursor
import javax.swing.*
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.LineEvent
import java.awt.Color
import java.awt.Font


fun ImageIcon.scaled(width: Int, height: Int): ImageIcon =//Scaling images for icons
    ImageIcon(image.getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH))

fun playSound(bytes: ByteArray) {// Plays a sound effect
    val stream = AudioSystem.getAudioInputStream(bytes.inputStream())
    AudioSystem.getClip().apply {
        open(stream)
        start()
        addLineListener { if (it.type == LineEvent.Type.STOP) close() }
    }
}

/**
 * Application entry point
 */
fun main() {
     UIManager.setLookAndFeel("com.jtattoo.plaf.luna.LunaLookAndFeel")   // Initialise the LAF

    val app = App()                 // Get an app state object
    val window = MainWindow(app)    // Spawn the UI, passing in the app state

    SwingUtilities.invokeLater { window.show() }
}

/**
 * Manage app state
 *
 * @property
 * @property
 */
class App {
    var cookiesFound = 0 //count of cookies that have been found

    fun cookieCollected() { //add a number to the count
        cookiesFound++
    }

    fun allFound(): Boolean {// return true when all possible cookies have been found & counted
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
    //icons
    val computerLockedIcon = ImageIcon(ClassLoader.getSystemResource("images/computer-locked-icon.png")).scaled(80, 80)
    val computerIcon = ImageIcon(ClassLoader.getSystemResource("images/computer-icon.png")).scaled(80, 80)
    val solitaireIcon = ImageIcon(ClassLoader.getSystemResource("images/solitaire-icon.png")).scaled(80, 80)
    val minesweeperIcon = ImageIcon(ClassLoader.getSystemResource("images/minesweeper-icon.png")).scaled(80, 80)
    val notesIcon = ImageIcon(ClassLoader.getSystemResource("images/notes-icon.png")).scaled(80, 80)
    val galleryIcon = ImageIcon(ClassLoader.getSystemResource("images/gallery-icon.png")).scaled(80, 80)
    val bonziIcon = ImageIcon(ClassLoader.getSystemResource("images/bonzi-icon.png")).scaled(80, 80)

    private val panel = JPanel().apply { layout = null }

    private val soundEffects: List<ByteArray> = listOf("click.wav", "error.wav", "startup.wav", "bgmusic.wav")//load sound effects
        .map { name -> ClassLoader.getSystemResourceAsStream("sounds/$name")!!.readBytes() }

    private var cookieLabel = JLabel("Cookies Collected: 0/5")
    private var bgLabel = JLabel()

    private val computerButton = JButton("My Computer", computerIcon)
    private val computerWindow = ComputerWindow(this, app)       // Pass app state to dialog too

    private val solitaireButton = JButton("Solitaire", solitaireIcon)
    private val solitaireWindow = SolitaireWindow(this, app)    // Pass app state to dialog too

    private val minesweeperButton = JButton("Minesweeper", minesweeperIcon)
    private val minesweeperWindow = MinesweeperWindow(this, app)    // Pass app state to dialog too

    private val notesButton = JButton("Notes", notesIcon)
    private val notesWindow = NotesWindow(this, app)    // Pass app state to dialog too

    private val galleryButton = JButton("Gallery", galleryIcon)
    private val galleryWindow = GalleryWindow(this, app)    // Pass app state to dialog too

    private val bonziButton = JButton("Bonzi Buddy", bonziIcon)
    private val bonziWindow = BonziWindow(this, app)    // Pass app state to dialog too

    private val winTimer = Timer(2000, null) //2-second timer

    init {
        setupWindow()
        setupLayout()
        setupStyles()
        setupActions()
        updateUI()
        tutorial()
    }

    private fun setupLayout() {
        panel.preferredSize = java.awt.Dimension(400, 1000)

        val bgIcon = ImageIcon(ClassLoader.getSystemResource("images/main-bg.png")).scaled(panel.width, panel.height)
        bgLabel.icon = bgIcon

        bgLabel.setBounds(0, 0, panel.width, panel.height)

        computerButton.setBounds(10, 10, 80, 100)
        computerButton.cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)//change cursor on hover

        solitaireButton.setBounds(10, 300, 80, 100)
        solitaireButton.cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)//change cursor on hover

        minesweeperButton.setBounds(10, 190, 80, 100)
        minesweeperButton.cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)//change cursor on hover

        notesButton.setBounds(10, 90, 80, 100)
        notesButton.cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)//change cursor on hover

        galleryButton.setBounds(10, 400, 80, 100)
        galleryButton.cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)//change cursor on hover

        bonziButton.setBounds(10, 500, 80, 100)
        bonziButton.cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)//change cursor on hover

        cookieLabel.setBounds((panel.width +250)*4/6, panel.height -30, 250, 20)

        panel.add(bgLabel)
        bgLabel.add(computerButton)
        bgLabel.add(solitaireButton)
        bgLabel.add(minesweeperButton)
        bgLabel.add(notesButton)
        bgLabel.add(galleryButton)
        bgLabel.add(bonziButton)
        bgLabel.add(cookieLabel)
    }

    private fun setupStyles() {

        computerButton.verticalTextPosition = SwingConstants.BOTTOM
        computerButton.horizontalTextPosition = SwingConstants.CENTER
        computerButton.isBorderPainted = false
        computerButton.isFocusPainted = false
        computerButton.isContentAreaFilled = false
        computerButton.isEnabled = false
        computerButton.disabledIcon = computerLockedIcon   // different icon when disabled

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

        notesButton.verticalTextPosition = SwingConstants.BOTTOM
        notesButton.horizontalTextPosition = SwingConstants.CENTER
        notesButton.isBorderPainted = false
        notesButton.isFocusPainted = false
        notesButton.isContentAreaFilled = false

        galleryButton.verticalTextPosition = SwingConstants.BOTTOM
        galleryButton.horizontalTextPosition = SwingConstants.CENTER
        galleryButton.isBorderPainted = false
        galleryButton.isFocusPainted = false
        galleryButton.isContentAreaFilled = false

        bonziButton.verticalTextPosition = SwingConstants.BOTTOM
        bonziButton.horizontalTextPosition = SwingConstants.CENTER
        bonziButton.isBorderPainted = false
        bonziButton.isFocusPainted = false
        bonziButton.isContentAreaFilled = false

        cookieLabel.foreground = Color.WHITE
        cookieLabel.font = Font(Font.MONOSPACED, Font.BOLD, 16)

        bgLabel.isVisible = true
    }

    private fun setupWindow() {
        frame.isResizable = false                           // Can't resize
        frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE  // Exit upon window close
        frame.contentPane = panel                           // Define the main content\
        frame.pack()
        val screenSize = java.awt.Toolkit.getDefaultToolkit().screenSize
        frame.setSize(screenSize.width, screenSize.height)//making it "full screen"
    }

    private fun setupActions() { //Provide actions for each button click
        computerButton.addActionListener { handleComClick() }
        solitaireButton.addActionListener { handleSolClick() }
        minesweeperButton.addActionListener { handleMineClick() }
        notesButton.addActionListener { handleNoteClick() }
        galleryButton.addActionListener { handleGalClick() }
        bonziButton.addActionListener { handleBonClick() }
        winTimer.addActionListener { handleWinTimer() }
    }

    /**
     * First win sequence, begins after timer delay
     */
    private fun handleWinTimer() {
        // We want to clear the screen now
        solitaireWindow.hide()
        minesweeperWindow.hide()
        notesWindow.hide()
        galleryWindow.hide()
        bonziWindow.hide()

        // Final feedback
        playSound(soundEffects[1])
        JOptionPane.showMessageDialog(null, "Malware discovered! All data has been scraped!", "Windows Defender",  JOptionPane.INFORMATION_MESSAGE )

        // Enable final window
        computerButton.isEnabled = true

        // Cancel so timer doesn't keep firing
        winTimer.stop()
    }

    /**
     * Series of dialog windows telling the user how to play
     */
    private fun tutorial() {
        playSound(soundEffects[1])
        JOptionPane.showMessageDialog(
            frame,
            "Virus Detected!!",
            "Error",
            JOptionPane.ERROR_MESSAGE
        )
        JOptionPane.showMessageDialog(
            frame,
            "It's you. You're the virus, and to fulfill your purpose you must shut this computer down!",
            "Error",
            JOptionPane.ERROR_MESSAGE
        )
        JOptionPane.showMessageDialog(
            frame,
            "Luckily, this users data is scattered all through their apps in the form of 'Cookies'",
            "Error",
            JOptionPane.ERROR_MESSAGE
        )
        JOptionPane.showMessageDialog(
            frame,
            "Find them hidden in plain sight, then use that data to shut down the whole system!",
            "Error",
            JOptionPane.ERROR_MESSAGE
        )
        JOptionPane.showMessageDialog(
            frame,
            "Good luck little malware!",
            "Error",
            JOptionPane.ERROR_MESSAGE
        )
        playSound(soundEffects[2])
        playSound(soundEffects[3])
    }

    /**
     * Computer click handler
     */
    private fun handleComClick(){
        playSound(soundEffects[0])
        computerWindow.dialog.setLocation(550,150)
        computerWindow.show()
    }

    /**
     * Solitaire click handler
     */
    private fun handleSolClick() {
        playSound(soundEffects[0])
        solitaireWindow.dialog.setLocation(550,150)
        solitaireWindow.show()
    }

    /**
     * Minesweeper click handler
     */
    private fun handleMineClick() {
        playSound(soundEffects[0])
        minesweeperWindow.dialog.setLocation(1200,200)
        minesweeperWindow.show()
    }

    /**
     * Notes app click handler
     */
    private fun handleNoteClick() {
        playSound(soundEffects[0])
        notesWindow.dialog.setLocation(200,100)
        notesWindow.show()
    }

    /**
     * Gallery click handler
     */
    private fun handleGalClick() {
        playSound(soundEffects[0])
        galleryWindow.dialog.setLocation(150,300)
        galleryWindow.show()
    }

    /**
     * Bonzi Buddy click handler
     */
    private fun handleBonClick() {
        playSound(soundEffects[0])
        bonziWindow.dialog.setLocation(800,400)
        bonziWindow.show()
    }

    fun updateUI() {
        cookieLabel.text = "Cookies Collected: ${app.cookiesFound}/5"//update cookie count display
    }

    fun show() {
        frame.isVisible = true
    }

    fun checkIfAllFound() { //check if all cookies have been found, if yes, begin winTimer if no, do nothing
        updateUI()

        if (app.allFound()) {
            winTimer.start()
        }
    }

    /**
     * Final win sequence that ends the game
     */
    fun shutDown() {
        val bluescreenIcon = ImageIcon(ClassLoader.getSystemResource("images/bluescreen.png")).scaled(frame.width, frame.height)

        if (app.allFound()) {
            //Hide any windows that may be open
            computerWindow.hide()
            solitaireWindow.hide()
            minesweeperWindow.hide()
            notesWindow.hide()
            galleryWindow.hide()
            bonziWindow.hide()

            //Final message (win message)
            JOptionPane.showMessageDialog(null, "Computer security compromised!! Immediate shutdown inevitable! Malware has completely taken over!", "FATAL ERROR",  JOptionPane.ERROR_MESSAGE )

            //Disable and hide all buttons & labels
            computerButton.isVisible = false
            solitaireButton.isVisible = false
            minesweeperButton.isVisible = false
            notesButton.isVisible = false
            galleryButton.isVisible = false
            bonziButton.isVisible = false
            cookieLabel.isVisible = false

            computerButton.isEnabled = false
            solitaireButton.isEnabled = false
            minesweeperButton.isEnabled = false
            notesButton.isEnabled = false
            galleryButton.isEnabled = false
            bonziButton.isEnabled = false

            //Show win screen
            bgLabel.icon = bluescreenIcon
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

    private val soundEffects: List<ByteArray> = listOf("click.wav", "cookiefound.wav", "glitch.wav")//load sound effects
        .map { name -> ClassLoader.getSystemResourceAsStream("sounds/$name")!!.readBytes() }

    private val backLabel = JLabel()
    private val targetButton = JButton()
    private val cookieButton = JButton()

    val bgBeforeIcon: ImageIcon
    val bgAfterIcon: ImageIcon
    val bgInfectedIcon: ImageIcon
    val cookieImageIcon: ImageIcon

    init {
        bgBeforeIcon = ImageIcon(ClassLoader.getSystemResource("images/solitaire-before.png")).scaled(600,450)
        bgAfterIcon = ImageIcon(ClassLoader.getSystemResource("images/solitaire-after.png")).scaled(600,450)
        bgInfectedIcon = ImageIcon(ClassLoader.getSystemResource("images/solitaire-infected.png")).scaled(600,450)
        cookieImageIcon = ImageIcon(ClassLoader.getSystemResource("images/cookie-4.png")).scaled(60, 60)

        setupLayout()
        setupStyles()
        setupActions()
        setupWindow()
    }

    fun hide() {
        dialog.isVisible = false
    }

    private fun setupLayout() {
        panel.preferredSize = java.awt.Dimension(600,450)

        backLabel.setBounds(0, 0, 600, 450)
        backLabel.icon = bgBeforeIcon

        targetButton.setBounds(20, 10, 70, 100)
        targetButton.cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)//cursor changes on hover

        cookieButton.setBounds(25, 30, 60, 60)
        cookieButton.cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)//cursor changes on hover
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
        targetButton.addActionListener {handleTargetClick()}
        cookieButton.addActionListener {handleCookieClick()}
    }

    /**
     * Handles discoverable item click
     */
    private fun handleTargetClick() {
        playSound(soundEffects[0])
        playSound(soundEffects[1])
        // Don't want them to click again
        targetButton.isEnabled = false
        // show found state
        backLabel.icon = bgAfterIcon
        // reveal cookie
        cookieButton.isVisible = true
    }

    /**
     * Handles cookie click
     */
    private fun handleCookieClick() {
        playSound(soundEffects[0])
        playSound(soundEffects[2])
        //Remove the cookie so they cannot collect it twice
        cookieButton.isEnabled = false
        cookieButton.isVisible = false
        // show infected state
        backLabel.icon = bgInfectedIcon
        // count the cookie
        app.cookieCollected()
        // check if all cookies found
        owner.checkIfAllFound()
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

    private val soundEffects: List<ByteArray> = listOf("click.wav", "cookiefound.wav", "glitch.wav")//load sound effects
        .map { name -> ClassLoader.getSystemResourceAsStream("sounds/$name")!!.readBytes() }

    private val backLabel = JLabel()
    private val targetButton = JButton()
    private val cookieButton = JButton()

    val bgBeforeIcon: ImageIcon
    val bgAfterIcon: ImageIcon
    val bgInfectedIcon: ImageIcon
    val cookieImageIcon: ImageIcon

    init {
        bgBeforeIcon = ImageIcon(ClassLoader.getSystemResource("images/minesweeper-before.png")).scaled(300,350)
        bgAfterIcon = ImageIcon(ClassLoader.getSystemResource("images/minesweeper-after.png")).scaled(300,350)
        bgInfectedIcon = ImageIcon(ClassLoader.getSystemResource("images/minesweeper-infected.png")).scaled(300,350)
        cookieImageIcon = ImageIcon(ClassLoader.getSystemResource("images/cookie-5.png")).scaled(125, 125)

        setupLayout()
        setupStyles()
        setupActions()
        setupWindow()
    }

    fun hide() {
        dialog.isVisible = false
    }

    private fun setupLayout() {
        panel.preferredSize = java.awt.Dimension(300,350)

        backLabel.setBounds(0, 0, 300, 350)
        backLabel.icon = bgBeforeIcon

        targetButton.setBounds(50, 100, 200, 200)
        targetButton.cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)//cursor changes on hover

        cookieButton.setBounds(85, 135, 125, 125)
        cookieButton.cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)//cursor changes on hover
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
        targetButton.addActionListener {handleTargetClick()}

        cookieButton.addActionListener {handleCookieClick()}
    }

    /**
     * Handles discoverable item click
     */
    private fun handleTargetClick() {
        playSound(soundEffects[0])
        playSound(soundEffects[1])
        // Don't want them to click again as it would show the cookie again
        targetButton.isEnabled = false
        // show found state
        backLabel.icon = bgAfterIcon
        // Show that cookie!
        cookieButton.isVisible = true
    }

    /**
     * Handles cookie click
     */
    private fun handleCookieClick() {
        playSound(soundEffects[0])
        playSound(soundEffects[2])
        //Remove the cookie so they cannot collect it twice
        cookieButton.isEnabled = false
        cookieButton.isVisible = false
        // show infected state
        backLabel.icon = bgInfectedIcon
        // count the cookie
        app.cookieCollected()
        // check if all cookies found
        owner.checkIfAllFound()
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
class NotesWindow(val owner: MainWindow, val app: App) {
    val dialog = JDialog(owner.frame, "Notes", false)
    private val panel = JPanel().apply { layout = null }

    private val soundEffects: List<ByteArray> = listOf("click.wav", "cookiefound.wav", "glitch.wav")//load sound effects
        .map { name -> ClassLoader.getSystemResourceAsStream("sounds/$name")!!.readBytes() }

    private val backLabel = JLabel()
    private val targetButton = JButton()
    private val cookieButton = JButton()

    val bgBeforeIcon: ImageIcon
    val bgAfterIcon: ImageIcon
    val bgInfectedIcon: ImageIcon
    val cookieImageIcon: ImageIcon

    init {
        bgBeforeIcon = ImageIcon(ClassLoader.getSystemResource("images/notes-before.png")).scaled(650,450)
        bgAfterIcon = ImageIcon(ClassLoader.getSystemResource("images/notes-after.png")).scaled(650,450)
        bgInfectedIcon = ImageIcon(ClassLoader.getSystemResource("images/notes-infected.png")).scaled(650,450)
        cookieImageIcon = ImageIcon(ClassLoader.getSystemResource("images/cookie-6.png")).scaled(50, 50)

        setupLayout()
        setupStyles()
        setupActions()
        setupWindow()
    }

    fun hide() {
        dialog.isVisible = false
    }

    private fun setupLayout() {
        panel.preferredSize = java.awt.Dimension(650,450)

        backLabel.setBounds(0, 0, 650, 450)
        backLabel.icon = bgBeforeIcon

        targetButton.setBounds(50, 200, 150, 50)
        targetButton.cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)//cursor changes on hover

        cookieButton.setBounds(80, 200, 50, 50)
        cookieButton.cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)//cursor changes on hover
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
        targetButton.addActionListener {handleTargetClick()}

        cookieButton.addActionListener {handleCookieClick()}
    }

    /**
     * Handles discoverable item click
     */
    private fun handleTargetClick() {
        playSound(soundEffects[0])
        playSound(soundEffects[1])
        // Don't want them to click again as it would show the cookie again
        targetButton.isEnabled = false
        // show found state
        backLabel.icon = bgAfterIcon
        // Show that cookie!
        cookieButton.isVisible = true
    }

    /**
     * Handles cookie click
     */
    private fun handleCookieClick() {
        playSound(soundEffects[0])
        playSound(soundEffects[2])
        //Remove the cookie so they cannot collect it twice
        cookieButton.isEnabled = false
        cookieButton.isVisible = false
        // show infected state
        backLabel.icon = bgInfectedIcon
        // count the cookie
        app.cookieCollected()
        // check if all cookies found
        owner.checkIfAllFound()
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
class GalleryWindow(val owner: MainWindow, val app: App) {
    val dialog = JDialog(owner.frame, "Gallery", false)
    private val panel = JPanel().apply { layout = null }

    private val soundEffects: List<ByteArray> = listOf("click.wav", "cookiefound.wav", "glitch.wav")//load sound effects
        .map { name -> ClassLoader.getSystemResourceAsStream("sounds/$name")!!.readBytes() }

    private val backLabel = JLabel()
    private val targetButton = JButton()
    private val cookieButton = JButton()

    val bgBeforeIcon: ImageIcon
    val bgAfterIcon: ImageIcon
    val bgInfectedIcon: ImageIcon
    val cookieImageIcon: ImageIcon

    init {
        bgBeforeIcon = ImageIcon(ClassLoader.getSystemResource("images/gallery-before.png")).scaled(650,450)
        bgAfterIcon = ImageIcon(ClassLoader.getSystemResource("images/gallery-after.png")).scaled(650,450)
        bgInfectedIcon = ImageIcon(ClassLoader.getSystemResource("images/gallery-infected.png")).scaled(650,450)
        cookieImageIcon = ImageIcon(ClassLoader.getSystemResource("images/cookie-7.png")).scaled(125, 125)

        setupLayout()
        setupStyles()
        setupActions()
        setupWindow()

    }

    fun hide() {
        dialog.isVisible = false
    }

    private fun setupLayout() {
        panel.preferredSize = java.awt.Dimension(650,450)

        backLabel.setBounds(0, 0, 650, 450)
        backLabel.icon = bgBeforeIcon

        targetButton.setBounds(530, 290, 70, 60)
        targetButton.cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)//cursor changes on hover

        cookieButton.setBounds(150, 275, 125, 125)
        cookieButton.cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)//cursor changes on hover
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
        targetButton.addActionListener {handleTargetClick()}

        cookieButton.addActionListener {handleCookieClick()}
    }

    /**
     * Handles discoverable item click
     */
    private fun handleTargetClick() {
        playSound(soundEffects[0])
        playSound(soundEffects[1])
        // Don't want them to click again as it would show the cookie again
        targetButton.isEnabled = false
        // show found state
        backLabel.icon = bgAfterIcon
        // Show that cookie!
        cookieButton.isVisible = true
    }

    /**
     * Handles cookie click
     */
    private fun handleCookieClick() {
        playSound(soundEffects[0])
        playSound(soundEffects[2])
        //Remove the cookie so they cannot collect it twice
        cookieButton.isEnabled = false
        cookieButton.isVisible = false
        // show infected state
        backLabel.icon = bgInfectedIcon
        // count the cookie
        app.cookieCollected()
        // check if all cookies found
        owner.checkIfAllFound()
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
class BonziWindow(val owner: MainWindow, val app: App) {
    val dialog = JDialog(owner.frame, "Bonzi Buddy Install", false)
    private val panel = JPanel().apply { layout = null }

    private val soundEffects: List<ByteArray> = listOf("click.wav", "cookiefound.wav", "glitch.wav")//load sound effects
        .map { name -> ClassLoader.getSystemResourceAsStream("sounds/$name")!!.readBytes() }

    private val backLabel = JLabel()
    private val targetButton = JButton()
    private val cookieButton = JButton()

    val bgBeforeIcon: ImageIcon
    val bgAfterIcon: ImageIcon
    val bgInfectedIcon: ImageIcon
    val cookieImageIcon: ImageIcon

    init {
        bgBeforeIcon = ImageIcon(ClassLoader.getSystemResource("images/bonzi-before.png")).scaled(650,400)
        bgAfterIcon = ImageIcon(ClassLoader.getSystemResource("images/bonzi-after.png")).scaled(650,400)
        bgInfectedIcon = ImageIcon(ClassLoader.getSystemResource("images/bonzi-infected.png")).scaled(650,400)
        cookieImageIcon = ImageIcon(ClassLoader.getSystemResource("images/cookie-8.png")).scaled(75, 75)

        setupLayout()
        setupStyles()
        setupActions()
        setupWindow()

    }

    fun hide() {
        dialog.isVisible = false
    }

    private fun setupLayout() {
        panel.preferredSize = java.awt.Dimension(650,400)

        backLabel.setBounds(0, 0, 650, 400)
        backLabel.icon = bgBeforeIcon

        targetButton.setBounds(200, 360, 140, 30)
        targetButton.cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)//cursor changes on hover

        cookieButton.setBounds(550, 170, 125, 125)
        cookieButton.cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)//cursor changes on hover
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
        targetButton.addActionListener {handleTargetClick()}

        cookieButton.addActionListener {handleCookieClick()}
    }

    /**
     * Handles discoverable item click
     */
    private fun handleTargetClick() {
        playSound(soundEffects[0])
        playSound(soundEffects[1])

        // Don't want them to click again as it would show the cookie again
        targetButton.isEnabled = false
        // show found state
        backLabel.icon = bgAfterIcon
        // Show that cookie!
        cookieButton.isVisible = true
    }

    /**
     * Handles cookie click
     */
    private fun handleCookieClick() {
        playSound(soundEffects[0])
        playSound(soundEffects[2])
        //Remove the cookie so they cannot collect it twice
        cookieButton.isEnabled = false
        cookieButton.isVisible = false
        // show infected state
        backLabel.icon = bgInfectedIcon
        // count the cookie
        app.cookieCollected()
        // check if all cookies found
        owner.checkIfAllFound()
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
 */
class ComputerWindow(val owner: MainWindow, val app: App) {
    val dialog = JDialog(owner.frame, "Computer Administrator Menu", false)
    private val panel = JPanel().apply { layout = null }

    private val soundEffects: List<ByteArray> = listOf("click.wav", "glitch.wav")//load sound effects
        .map { name -> ClassLoader.getSystemResourceAsStream("sounds/$name")!!.readBytes() }

    private val backLabel = JLabel()
    private val targetButton = JButton()

    val bgIcon: ImageIcon

    init {
        bgIcon = ImageIcon(ClassLoader.getSystemResource("images/computer-bg.png")).scaled(450,600)

        setupLayout()
        setupStyles()
        setupActions()
        setupWindow()
    }

    fun hide() {
        dialog.isVisible = false
    }

    private fun setupLayout() {
        panel.preferredSize = java.awt.Dimension(450,600)

        backLabel.setBounds(0, 0, 450, 600)
        backLabel.icon = bgIcon

        targetButton.setBounds(300, 500, 250, 60)
        targetButton.cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)//cursor changes on hover

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
        targetButton.addActionListener {handleTargetClick()}
    }

    /**
     * Handles discoverable item click
     */
    private fun handleTargetClick() {
        playSound(soundEffects[0])
        playSound(soundEffects[1])
        // run final win messages
        owner.shutDown()
        // Don't allow them to click it again
        targetButton.isEnabled = false
    }

    fun show() {
        dialog.isVisible = true
    }
}