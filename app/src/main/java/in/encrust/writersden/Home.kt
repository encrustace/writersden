package `in`.encrust.writersden

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.StrictMode
import android.util.TypedValue
import android.view.GestureDetector
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import yuku.ambilwarna.AmbilWarnaDialog
import java.io.*
import java.util.*
import kotlin.Boolean
import kotlin.Float
import kotlin.Int
import kotlin.IntArray
import kotlin.String
import kotlin.collections.ArrayList
import kotlin.Array as Array1
import kotlin.arrayOf as arrayOf1

class Home : AppCompatActivity() {
    private var adView: AdView? = null
    private var imageList: ArrayList<Drawable>? = null
    private var fontList: ArrayList<Typeface>? = null
    private var fontNamesList: List<String>? = null
    private var position: Int = 0
    private var fontTextView: TextView? = null
    private var imageConst: ConstraintLayout? = null
    private var itemConst: ConstraintLayout? = null
    private var stylingConst: ConstraintLayout? = null
    private var xCord: Float = 0.toFloat()
    private var yCord: Float = 0.toFloat()
    private var imageView: ImageView? = null
    private var sizeMinus: Button? = null
    private var seekBarSize: SeekBar? = null
    private var sizePlus: Button? = null
    private var rotateMinus: Button? = null
    private var seekBarRotate: SeekBar? = null
    private var rotatePlus: Button? = null
    private var spaceMinus: Button? = null
    private var seekBarSpace: SeekBar? = null
    private var spacePlus: Button? = null
    private var shadowMinus: Button? = null
    private var seekBarShadow: SeekBar? = null
    private var shadowPlus: Button? = null
    private var id = 0
    private var textArray: ArrayList<TextView>? = null
    private var currentText: TextView? = null
    private var recyclerView: RecyclerView? = null
    private var gestureDetector: GestureDetector? = null

    //Show size, angle, etc
    private var showSize: TextView? = null
    private var showAngle: TextView? = null
    private var showGap: TextView? = null
    private var showShadow: TextView? = null

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home)

        adView = findViewById(R.id.adView)
        imageConst = findViewById(R.id.save_cost)
        imageView = findViewById(R.id.home_image)
        itemConst = findViewById(R.id.item_const)
        val shareButton = findViewById<Button>(R.id.home_share)
        val imageButton = findViewById<ImageButton>(R.id.image_button)
        val textButton = findViewById<ImageButton>(R.id.text_button)
        recyclerView = findViewById(R.id.home_recycler)
        stylingConst = findViewById(R.id.styling_const)
        val colorButton = findViewById<Button>(R.id.text_color)
        val fontButton = findViewById<Button>(R.id.text_font)
        val editButton = findViewById<Button>(R.id.edit_text)
        sizeMinus = findViewById(R.id.seek_size_minus)
        seekBarSize = findViewById(R.id.seek_size)
        sizePlus = findViewById(R.id.seek_size_plus)
        rotateMinus = findViewById(R.id.seek_rotate_minus)
        seekBarRotate = findViewById(R.id.seek_rotate)
        rotatePlus = findViewById(R.id.seek_rotate_plus)
        spaceMinus = findViewById(R.id.seek_gap_minus)
        seekBarSpace = findViewById(R.id.seek_gap)
        spacePlus = findViewById(R.id.seek_gap_plus)
        shadowMinus = findViewById(R.id.seek_shadow_minus)
        seekBarShadow = findViewById(R.id.seek_shadow)
        shadowPlus = findViewById(R.id.seek_shadow_plus)
        gestureDetector = GestureDetector(this, SingleTapConfirm())

        showSize = findViewById(R.id.seek_size_show)
        showAngle = findViewById(R.id.seek_rotate_show)
        showGap = findViewById(R.id.seek_gap_show)
        showShadow = findViewById(R.id.seek_shadow_show)


        textArray = ArrayList()
        textArray!!.add(TextView(this))
        textArray!!.add(TextView(this))
        textArray!!.add(TextView(this))
        textArray!!.add(TextView(this))
        textArray!!.add(TextView(this))

        makeImageList()
        makeFontList()
        stylingConst!!.visibility = View.INVISIBLE

        selectImage(1)

        askPermissions()
        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())

        shareButton.setOnClickListener {
            val bitmap = takeScreenshot()
            saveBitmap(bitmap)
            //save
        }

        val adRequest = AdRequest.Builder().build()
        adView!!.loadAd(adRequest)

        textButton.setOnClickListener { createTextBuilder() }

        imageButton.setOnClickListener {
            recyclerView!!.visibility = View.VISIBLE
            itemConst!!.visibility = View.INVISIBLE
            loadImages()
        }

        imageConst!!.setOnClickListener {
            textArray!![0].background = null
            textArray!![1].background = null
            textArray!![2].background = null
            textArray!![3].background = null
            textArray!![4].background = null
            stylingConst!!.visibility = View.INVISIBLE
            itemConst!!.visibility = View.VISIBLE
            recyclerView!!.visibility = View.INVISIBLE
        }


        /////Move & decoration///////////////////

        textArray!![0].setOnTouchListener(View.OnTouchListener { v, event ->
            if (gestureDetector!!.onTouchEvent(event)) {

                if (stylingConst!!.isShown) {
                    stylingConst!!.visibility = View.INVISIBLE
                    textArray!![0].background = null
                    itemConst!!.visibility = View.VISIBLE
                } else {
                    stylingConst!!.visibility = View.VISIBLE
                    textArray!![0].background = getDrawable(R.drawable.highlight)
                    textArray!![1].background = null
                    textArray!![2].background = null
                    textArray!![3].background = null
                    textArray!![4].background = null
                    itemConst!!.visibility = View.INVISIBLE
                    currentText = textArray!![0]
                    seekBarSize!!.progress = currentText!!.textSize.toInt()
                    showSize!!.text = currentText!!.textSize.toInt().toString()
                    seekBarRotate!!.progress = currentText!!.rotation.toInt()
                    showAngle!!.text = currentText!!.rotation.toInt().toString()
                    seekBarSpace!!.progress = currentText!!.lineSpacingExtra.toInt()
                    showGap!!.text = currentText!!.lineSpacingExtra.toInt().toString()
                    seekBarShadow!!.progress = currentText!!.shadowDx.toInt()
                    showShadow!!.text = currentText!!.shadowDx.toInt().toString()
                }

                true
            } else {

                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        xCord = v.x - event.rawX
                        yCord = v.y - event.rawY
                    }

                    MotionEvent.ACTION_MOVE -> v.animate()
                            .x(event.rawX + xCord)
                            .y(event.rawY + yCord)
                            .setDuration(0)
                            .start()

                    else -> return@OnTouchListener false
                }
                false
            }
        })

        textArray!![1].setOnTouchListener(View.OnTouchListener { v, event ->
            if (gestureDetector!!.onTouchEvent(event)) {

                if (stylingConst!!.isShown) {
                    stylingConst!!.visibility = View.INVISIBLE
                    textArray!![1].background = null
                    itemConst!!.visibility = View.VISIBLE
                } else {
                    stylingConst!!.visibility = View.VISIBLE
                    textArray!![1].background = getDrawable(R.drawable.highlight)
                    textArray!![0].background = null
                    textArray!![2].background = null
                    textArray!![3].background = null
                    textArray!![4].background = null
                    itemConst!!.visibility = View.INVISIBLE
                    currentText = textArray!![1]
                    seekBarSize!!.progress = currentText!!.textSize.toInt()
                    showSize!!.text = currentText!!.textSize.toInt().toString()
                    seekBarRotate!!.progress = currentText!!.rotation.toInt()
                    showAngle!!.text = currentText!!.rotation.toInt().toString()
                    seekBarSpace!!.progress = currentText!!.lineSpacingExtra.toInt()
                    showGap!!.text = currentText!!.lineSpacingExtra.toInt().toString()
                    seekBarShadow!!.progress = currentText!!.shadowDx.toInt()
                    showShadow!!.text = currentText!!.shadowDx.toInt().toString()
                }

                true
            } else {

                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        xCord = v.x - event.rawX
                        yCord = v.y - event.rawY
                    }

                    MotionEvent.ACTION_MOVE -> v.animate()
                            .x(event.rawX + xCord)
                            .y(event.rawY + yCord)
                            .setDuration(0)
                            .start()

                    else -> return@OnTouchListener false
                }
                false
            }
        })


        textArray!![2].setOnTouchListener(View.OnTouchListener { v, event ->
            if (gestureDetector!!.onTouchEvent(event)) {

                if (stylingConst!!.isShown) {
                    stylingConst!!.visibility = View.INVISIBLE
                    textArray!![2].background = null
                    itemConst!!.visibility = View.VISIBLE
                } else {
                    stylingConst!!.visibility = View.VISIBLE
                    textArray!![2].background = getDrawable(R.drawable.highlight)
                    textArray!![0].background = null
                    textArray!![1].background = null
                    textArray!![3].background = null
                    textArray!![4].background = null
                    itemConst!!.visibility = View.INVISIBLE
                    currentText = textArray!![2]
                    seekBarSize!!.progress = currentText!!.textSize.toInt()
                    showSize!!.text = currentText!!.textSize.toInt().toString()
                    seekBarRotate!!.progress = currentText!!.rotation.toInt()
                    showAngle!!.text = currentText!!.rotation.toInt().toString()
                    seekBarSpace!!.progress = currentText!!.lineSpacingExtra.toInt()
                    showGap!!.text = currentText!!.lineSpacingExtra.toInt().toString()
                    seekBarShadow!!.progress = currentText!!.shadowDx.toInt()
                    showShadow!!.text = currentText!!.shadowDx.toInt().toString()
                }

                true
            } else {

                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        xCord = v.x - event.rawX
                        yCord = v.y - event.rawY
                    }

                    MotionEvent.ACTION_MOVE -> v.animate()
                            .x(event.rawX + xCord)
                            .y(event.rawY + yCord)
                            .setDuration(0)
                            .start()

                    else -> return@OnTouchListener false
                }
                false
            }
        })


        textArray!![3].setOnTouchListener(View.OnTouchListener { v, event ->
            if (gestureDetector!!.onTouchEvent(event)) {

                if (stylingConst!!.isShown) {
                    stylingConst!!.visibility = View.INVISIBLE
                    textArray!![3].background = null
                    itemConst!!.visibility = View.VISIBLE
                } else {
                    stylingConst!!.visibility = View.VISIBLE
                    textArray!![3].background = getDrawable(R.drawable.highlight)
                    textArray!![0].background = null
                    textArray!![1].background = null
                    textArray!![2].background = null
                    textArray!![4].background = null
                    itemConst!!.visibility = View.INVISIBLE
                    currentText = textArray!![3]
                    seekBarSize!!.progress = currentText!!.textSize.toInt()
                    showSize!!.text = currentText!!.textSize.toInt().toString()
                    seekBarRotate!!.progress = currentText!!.rotation.toInt()
                    showAngle!!.text = currentText!!.rotation.toInt().toString()
                    seekBarSpace!!.progress = currentText!!.lineSpacingExtra.toInt()
                    showGap!!.text = currentText!!.lineSpacingExtra.toInt().toString()
                    seekBarShadow!!.progress = currentText!!.shadowDx.toInt()
                    showShadow!!.text = currentText!!.shadowDx.toInt().toString()
                }

                true
            } else {

                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        xCord = v.x - event.rawX
                        yCord = v.y - event.rawY
                    }

                    MotionEvent.ACTION_MOVE -> v.animate()
                            .x(event.rawX + xCord)
                            .y(event.rawY + yCord)
                            .setDuration(0)
                            .start()

                    else -> return@OnTouchListener false
                }
                false
            }
        })

        textArray!![4].setOnTouchListener(View.OnTouchListener { v, event ->
            if (gestureDetector!!.onTouchEvent(event)) {

                if (stylingConst!!.isShown) {
                    stylingConst!!.visibility = View.INVISIBLE
                    textArray!![4].background = null
                    itemConst!!.visibility = View.VISIBLE
                } else {
                    stylingConst!!.visibility = View.VISIBLE
                    textArray!![4].background = getDrawable(R.drawable.highlight)
                    textArray!![0].background = null
                    textArray!![1].background = null
                    textArray!![2].background = null
                    textArray!![3].background = null
                    itemConst!!.visibility = View.INVISIBLE
                    currentText = textArray!![4]
                    seekBarSize!!.progress = currentText!!.textSize.toInt()
                    showSize!!.text = currentText!!.textSize.toInt().toString()
                    seekBarRotate!!.progress = currentText!!.rotation.toInt()
                    showAngle!!.text = currentText!!.rotation.toInt().toString()
                    seekBarSpace!!.progress = currentText!!.lineSpacingExtra.toInt()
                    showGap!!.text = currentText!!.lineSpacingExtra.toInt().toString()
                    seekBarShadow!!.progress = currentText!!.shadowDx.toInt()
                    showShadow!!.text = currentText!!.shadowDx.toInt().toString()
                }

                true
            } else {

                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        xCord = v.x - event.rawX
                        yCord = v.y - event.rawY
                    }

                    MotionEvent.ACTION_MOVE -> v.animate()
                            .x(event.rawX + xCord)
                            .y(event.rawY + yCord)
                            .setDuration(0)
                            .start()

                    else -> return@OnTouchListener false
                }
                false
            }
        })

        /////Move & decoration end///////////////////

        //Edit text
        editButton.setOnClickListener {
            editTextMethod(currentText!!.text.toString(), currentText!!.gravity)
        }

        //Text Size
        sizeMinus!!.setOnClickListener {
            seekBarSize!!.progress = currentText!!.textSize.toInt() - 1
            showSize!!.text = currentText!!.textSize.toInt().toString()
        }

        seekBarSize!!.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            @SuppressLint("SetTextI18n")
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                currentText!!.setTextSize(TypedValue.COMPLEX_UNIT_PX, progress.toFloat())
                showSize!!.text = currentText!!.textSize.toInt().toString()

            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {

            }
        })

        sizePlus!!.setOnClickListener {
            seekBarSize!!.progress = currentText!!.textSize.toInt() + 1
            showSize!!.text = currentText!!.textSize.toInt().toString()
        }

        //Text rotation
        rotateMinus!!.setOnClickListener {
            seekBarRotate!!.progress = currentText!!.rotation.toInt() - 1
            showAngle!!.text = currentText!!.rotation.toInt().toString()
        }

        seekBarRotate!!.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            @SuppressLint("SetTextI18n")
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                currentText!!.rotation = progress.toFloat()
                showAngle!!.text = currentText!!.rotation.toInt().toString()

            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {

            }
        })

        rotatePlus!!.setOnClickListener {
            seekBarRotate!!.progress = currentText!!.rotation.toInt() + 1
            showAngle!!.text = currentText!!.rotation.toInt().toString()
        }

        //Text Spacing
        spaceMinus!!.setOnClickListener {
            currentText!!.setLineSpacing(currentText!!.lineSpacingExtra - 1, 1F)
            seekBarSpace!!.progress = currentText!!.lineSpacingExtra.toInt()
            showGap!!.text = currentText!!.lineSpacingExtra.toInt().toString()
        }

        seekBarSpace!!.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            @SuppressLint("SetTextI18n")
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                currentText!!.setLineSpacing(progress.toFloat(), 1F)
                showGap!!.text = currentText!!.lineSpacingExtra.toInt().toString()

            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {

            }
        })

        spacePlus!!.setOnClickListener {
            currentText!!.setLineSpacing(currentText!!.lineSpacingExtra + 1, 1F)
            seekBarSpace!!.progress = currentText!!.lineSpacingExtra.toInt()
            showGap!!.text = currentText!!.lineSpacingExtra.toInt().toString()
        }

        //Text Shadow
        shadowMinus!!.setOnClickListener {
            currentText!!.setShadowLayer(currentText!!.shadowRadius - 1, currentText!!.shadowDx - 1, currentText!!.shadowDy - 1, Color.parseColor("#FF000000"))
            seekBarShadow!!.progress = currentText!!.shadowDx.toInt()
            showShadow!!.text = currentText!!.shadowDx.toInt().toString()
        }

        seekBarShadow!!.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            @SuppressLint("SetTextI18n")
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                currentText!!.setShadowLayer(progress.toFloat(), progress.toFloat(), progress.toFloat(), Color.parseColor("#FF000000"))
                showShadow!!.text = currentText!!.shadowDx.toInt().toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {

            }
        })

        shadowPlus!!.setOnClickListener {
            currentText!!.setShadowLayer(currentText!!.shadowRadius + 1, currentText!!.shadowDx + 1, currentText!!.shadowDy + 1, Color.parseColor("#FF000000"))
            seekBarShadow!!.progress = currentText!!.shadowDx.toInt()
            showShadow!!.text = currentText!!.shadowDx.toInt().toString()
        }

        //Change color
        colorButton.setOnClickListener {
            openColorPicker()
        }

        //Font changer
        fontButton.setOnClickListener {
            openFontChanger(currentText!!.text.toString())
        }

    }

    private fun takeScreenshot(): Bitmap {
        val b = Bitmap.createBitmap(imageConst!!.height, imageConst!!.width, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(b)
        imageConst!!.draw(canvas)
        return b
    }

    private fun saveBitmap(bitmap: Bitmap) {

        var n = 0
        var path = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "wd$n.jpg")
        while (true) {
            if (path.exists()) {
                n++
                path = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "wd$n.jpg")
            } else {
                break
            }
        }
        val fileOutputStream: FileOutputStream
        try {
            fileOutputStream = FileOutputStream(path)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)
            fileOutputStream.flush()
            fileOutputStream.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        Toast.makeText(this, "Saved in Gallery", Toast.LENGTH_SHORT).show()
        val uri = Uri.fromFile(path)
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "image/png"
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "")
        shareIntent.putExtra(Intent.EXTRA_TEXT, "")
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri)
        startActivity(Intent.createChooser(shareIntent, "Share Image"))

    }

    private fun openColorPicker() {

        val colorPicker = AmbilWarnaDialog(this, currentText!!.currentTextColor, object : AmbilWarnaDialog.OnAmbilWarnaListener {
            override fun onCancel(dialog: AmbilWarnaDialog) {

            }

            override fun onOk(dialog: AmbilWarnaDialog, color: Int) {

                currentText!!.setTextColor(color)
            }
        })
        colorPicker.show()

    }

    private fun editTextMethod(text: String, gravity: Int) {
        val builder = AlertDialog.Builder(this)
        val editId = 0
        val leftButtonId = 1
        val centerButtonId = 2
        val rightButtonId = 3
        val parent = 4

        val editConstraint = ConstraintLayout(this)
        val editConstParams = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_CONSTRAINT)
        editConstraint.minHeight = 300
        editConstraint.id = parent
        editConstraint.layoutParams = editConstParams

        val dEditText = EditText(this)
        val dEditTextParams = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT)
        dEditText.layoutParams = dEditTextParams
        dEditText.id = editId
        dEditText.gravity = gravity
        dEditText.setText(text)

        val leftButtonParams = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT)
        val leftButton = Button(this)
        leftButton.layoutParams = leftButtonParams
        leftButton.background = ContextCompat.getDrawable(applicationContext, R.drawable.ic_align_left)
        leftButton.id = leftButtonId

        val centerButton = Button(this)
        val centreButtonParams = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT)
        centerButton.layoutParams = centreButtonParams
        centerButton.background = ContextCompat.getDrawable(applicationContext, R.drawable.ic_align_center)
        centerButton.id = centerButtonId

        val rightButton = Button(this)
        val rightButtonParams = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT)
        rightButton.layoutParams = rightButtonParams
        rightButton.background = ContextCompat.getDrawable(applicationContext, R.drawable.ic_align_right)
        rightButton.id = rightButtonId


        editConstraint.addView(dEditText)
        editConstraint.addView(leftButton)
        editConstraint.addView(centerButton)
        editConstraint.addView(rightButton)

        val editSet = ConstraintSet()
        editSet.clone(editConstraint)

        editSet.connect(dEditText.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
        editSet.connect(dEditText.id, ConstraintSet.BOTTOM, centerButton.id, ConstraintSet.TOP)

        editSet.connect(leftButton.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
        editSet.connect(leftButton.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)

        editSet.connect(centerButton.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
        editSet.connect(centerButton.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
        editSet.connect(centerButton.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)

        editSet.connect(rightButton.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
        editSet.connect(rightButton.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)

        editSet.applyTo(editConstraint)

        builder.setView(editConstraint)
        builder.setPositiveButton("Submit") { _, _ ->
            currentText!!.text = dEditText.text
            currentText!!.gravity = dEditText.gravity
        }.setNegativeButton("Cancel") { _, _ -> }

        leftButton.setOnClickListener { dEditText.gravity = Gravity.START }

        centerButton.setOnClickListener { dEditText.gravity = Gravity.CENTER }

        rightButton.setOnClickListener { dEditText.gravity = Gravity.END }

        builder.show()

    }

    @SuppressLint("SetTextI18n")
    private fun createTextBuilder() {
        if (id < 5) {

            val builder = AlertDialog.Builder(this)
            val editId = 0
            val leftButtonId = 1
            val centerButtonId = 2
            val rightButtonId = 3
            val parent = 4

            val editConstraint = ConstraintLayout(this)
            val editConstParams = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_CONSTRAINT)
            editConstraint.minHeight = 300
            editConstraint.id = parent
            editConstraint.layoutParams = editConstParams

            val dEditText = EditText(this)
            val dEditTextParams = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT)
            dEditText.layoutParams = dEditTextParams
            dEditText.id = editId
            dEditText.gravity = Gravity.CENTER
            dEditText.hint = "Type here"

            val leftButtonParams = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT)
            val leftButton = Button(this)
            leftButton.layoutParams = leftButtonParams
            leftButton.background = ContextCompat.getDrawable(applicationContext, R.drawable.ic_align_left)
            leftButton.id = leftButtonId

            val centerButton = Button(this)
            val centreButtonParams = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT)
            centerButton.layoutParams = centreButtonParams
            centerButton.background = ContextCompat.getDrawable(applicationContext, R.drawable.ic_align_center)
            centerButton.id = centerButtonId

            val rightButton = Button(this)
            val rightButtonParams = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT)
            rightButton.layoutParams = rightButtonParams
            rightButton.background = ContextCompat.getDrawable(applicationContext, R.drawable.ic_align_right)
            rightButton.id = rightButtonId


            editConstraint.addView(dEditText)
            editConstraint.addView(leftButton)
            editConstraint.addView(centerButton)
            editConstraint.addView(rightButton)

            val editSet = ConstraintSet()
            editSet.clone(editConstraint)

            editSet.connect(dEditText.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
            editSet.connect(dEditText.id, ConstraintSet.BOTTOM, centerButton.id, ConstraintSet.TOP)

            editSet.connect(leftButton.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
            editSet.connect(leftButton.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)

            editSet.connect(centerButton.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
            editSet.connect(centerButton.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
            editSet.connect(centerButton.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)

            editSet.connect(rightButton.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
            editSet.connect(rightButton.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)

            editSet.applyTo(editConstraint)

            builder.setView(editConstraint)
            builder.setCancelable(false)

            builder.setPositiveButton("Submit") { _, _ ->
                if (dEditText.text.toString() == "") {
                    Toast.makeText(this, "Type something", Toast.LENGTH_LONG).show()
                    return@setPositiveButton
                } else {
                    createText(dEditText.text.toString(), dEditText.gravity)
                }
            }

            builder.setNegativeButton("Cancel") { _, _ -> }

            leftButton.setOnClickListener { dEditText.gravity = Gravity.START }

            centerButton.setOnClickListener { dEditText.gravity = Gravity.CENTER }

            rightButton.setOnClickListener { dEditText.gravity = Gravity.END }

            builder.show()

        } else {
            Toast.makeText(this, "Maximum 5 texts you can use", Toast.LENGTH_SHORT).show()
        }

    }

    private fun createText(text: String, gravity: Int?) {

        val set = ConstraintSet()
        set.clone(imageConst!!)
        textArray!![id].text = text
        textArray!![id].id = id
        textArray!![id].isClickable = true
        textArray!![id].setPadding(10, 10, 10, 10)
        textArray!![id].gravity = gravity!!
        textArray!![id].paintFlags = View.INVISIBLE
        textArray!![id].setTextColor(Color.parseColor("#FF000000"))
        imageConst!!.addView(textArray!![id])
        set.connect(textArray!![id].id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0)
        set.connect(textArray!![id].id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0)
        set.connect(textArray!![id].id, ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT, 0)
        set.connect(textArray!![id].id, ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT, 0)
        set.constrainHeight(textArray!![id].id, ConstraintSet.WRAP_CONTENT)
        set.constrainWidth(textArray!![id].id, ConstraintSet.MATCH_CONSTRAINT)
        set.applyTo(imageConst!!)
        id += 1
    }

    fun openGallery() {
        CropImage.startPickImageActivity(this)
    }

    @SuppressLint("NewApi")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val imageUri = CropImage.getPickImageResultUri(this, data)
            startCropImageActivity(imageUri)
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val imageUri = CropImage.getActivityResult(data)
            if (data != null) {
                imageView!!.setImageURI(imageUri.uri)
                recyclerView!!.visibility = View.INVISIBLE
                itemConst!!.visibility = View.VISIBLE
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(this, "Cropping failed" + imageUri.error, Toast.LENGTH_LONG).show()
            }
        }
    }


    private fun startCropImageActivity(image: Uri) {
        CropImage.activity(image)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1, 1)
                .setMultiTouchEnabled(true)
                .start(this)
    }

    fun selectImage(i: Int) {
        imageView!!.setImageDrawable(imageList!![i])
    }

    private fun loadImages() {

        val linearLayoutManager = LinearLayoutManager(applicationContext)
        linearLayoutManager.orientation = RecyclerView.HORIZONTAL
        recyclerView!!.layoutManager = linearLayoutManager
        val imageAdapter = ImageAdapter(this, imageList)
        recyclerView!!.adapter = imageAdapter
    }


    //////////////////////////////////////////////////////////////////////////////////

    private fun makeImageList() {
        imageList = ArrayList()
        val assetManager = assets
        var inputStream: InputStream?

        for (i in 1..6) {

            try {
                inputStream = assetManager.open("images/image$i.jpg")
                imageList!!.add(Drawable.createFromStream(inputStream, ""))
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
    }
    /////////////////////////////////////////////////////

    private fun makeFontList() {
        fontList = ArrayList()
        fontNamesList = ArrayList()
        val assetManager = this.assets

        try {
            val files = assetManager.list("fonts")
            fontNamesList = LinkedList(Arrays.asList(*files!!))
        } catch (e: IOException) {
            e.printStackTrace()
        }

        for (i in fontNamesList!!.indices) {
            fontList!!.add(Typeface.createFromAsset(assets, "fonts/" + fontNamesList!![i]))

        }
    }

    private fun openFontChanger(text: String) {

        val builder = AlertDialog.Builder(this)
        val id1 = 0
        val id2 = 1
        val id3 = 2

        val fontConst = ConstraintLayout(this)
        val fontConstParams = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT)
        fontConst.layoutParams = fontConstParams
        fontConst.id = id1

        fontTextView = TextView(this)
        val fontTextViewParams = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT)
        fontTextView!!.layoutParams = fontTextViewParams
        fontTextView!!.id = id2
        fontTextView!!.maxLines = 1
        fontTextView!!.text = text
        fontTextView!!.gravity = Gravity.CENTER
        fontTextView!!.setTextColor(ContextCompat.getColor(applicationContext, R.color.colorWhite))
        fontTextView!!.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.colorAccent))
        fontTextView!!.textSize = 40f

        val fontRecycler = RecyclerView(this)
        val fontRecyclerParams = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT)
        fontRecycler.layoutParams = fontRecyclerParams
        fontRecycler.id = id3

        fontConst.addView(fontRecycler)
        fontConst.addView(fontTextView)

        val fontSet = ConstraintSet()
        fontSet.clone(fontConst)
        fontSet.connect(fontTextView!!.id, ConstraintSet.TOP, fontConst.id, ConstraintSet.TOP)
        fontSet.connect(fontTextView!!.id, ConstraintSet.BOTTOM, fontConst.id, ConstraintSet.BOTTOM)

        fontSet.connect(fontRecycler.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
        fontSet.connect(fontRecycler.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
        fontSet.connect(fontRecycler.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)

        fontSet.applyTo(fontConst)

        val linearLayoutManager = LinearLayoutManager(applicationContext)
        linearLayoutManager.orientation = RecyclerView.VERTICAL
        fontRecycler.layoutManager = linearLayoutManager

        val fontAdapter = FontAdapter(this, fontList, fontNamesList)
        fontRecycler.adapter = fontAdapter

        builder.setView(fontConst)
        builder.setPositiveButton("Submit") { _, _ -> currentText!!.typeface = fontList!![position] }.setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }

        builder.show()
    }

    fun updateBroadCast(i: Int) {
        ////Broadcaster
        val mReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                position = intent.getIntExtra("position", 0)
            }
        }
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, IntentFilter("intent"))
        //////////////////till here//////////
        fontTextView!!.typeface = fontList!![i]
    }

    private fun askPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            } else {
                ActivityCompat.requestPermissions(this,
                        arrayOf1(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        1)
            }

        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array1<String>, grantResults: IntArray) {
        when (requestCode) {
            1 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Permission Granted
                } else {
                    //Not  granted
                }
            }
        }
    }

    override fun onBackPressed() {
        AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setNegativeButton("No", null)
                .setPositiveButton("Yes") { _, _ -> super@Home.onBackPressed() }
                .show()
    }
}
