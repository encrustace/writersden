package in.encrust.writersden;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.os.Vibrator;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import yuku.ambilwarna.AmbilWarnaDialog;

public class Home extends AppCompatActivity {
    final static int GALLERY_PICK = 1;
    private ArrayList<Drawable> imageList;
    private ArrayList<Typeface> fontList;
    private List<String> fontNamesList;
    private int position;
    private TextView fontTextView;
    private ConstraintLayout imageConst, itemConst, stylingConst;
    private float x_cord, y_cord;
    private ImageView imageView;
    private Button sizeButton;
    private Button rotateButton;
    private SeekBar seekBarSize, seekBarRotate;
    private int id = 0;
    private Vibrator vibrator;
    private TextView textArray[];
    private TextView currentText;
    private RecyclerView recyclerView;
    private GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        imageConst = findViewById(R.id.home_imageconst);
        imageView = findViewById(R.id.home_image);
        itemConst = findViewById(R.id.home_itemconst);
        Button donateButton = findViewById(R.id.home_donate);
        Button shareButton = findViewById(R.id.home_share);
        ImageButton imageButton = findViewById(R.id.home_imagebutton);
        ImageButton textButton = findViewById(R.id.home_textbutton);
        recyclerView = findViewById(R.id.home_recycler);
        stylingConst = findViewById(R.id.home_stylingconst);
        vibrator = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        Button colorButton = findViewById(R.id.home_textcolor);
        sizeButton = findViewById(R.id.home_textsize);
        Button fontButton = findViewById(R.id.home_textfont);
        Button removeButton = findViewById(R.id.home_textremove);
        Button editButton = findViewById(R.id.home_edittext);
        rotateButton = findViewById(R.id.home_textrotate);
        seekBarSize = findViewById(R.id.home_seekbarsize);
        seekBarRotate = findViewById(R.id.home_seekbarrotate);
        gestureDetector = new GestureDetector(this, new SingleTapConfirm());


        textArray = new TextView[5];
        textArray[0] = new TextView(this);
        textArray[1] = new TextView(this);
        textArray[2] = new TextView(this);
        textArray[3] = new TextView(this);
        textArray[4] = new TextView(this);

        makeImageList();
        makeFontList();
        stylingConst.setVisibility(View.INVISIBLE);
        seekBarSize.setVisibility(View.INVISIBLE);
        seekBarRotate.setVisibility(View.INVISIBLE);

        selectImage(1);

        askPermissions();
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askName();
                //save
            }
        });

        textButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createTextBuilder();
            }
        });

        donateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                donateDev();
            }
        });

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.setVisibility(View.VISIBLE);
                itemConst.setVisibility(View.INVISIBLE);
                loadImages();
            }
        });

        imageConst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textArray[0].setBackground(null);
                textArray[1].setBackground(null);
                textArray[2].setBackground(null);
                textArray[3].setBackground(null);
                textArray[4].setBackground(null);
                stylingConst.setVisibility(View.INVISIBLE);
                itemConst.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.INVISIBLE);
                seekBarRotate.setVisibility(View.INVISIBLE);
                seekBarSize.setVisibility(View.INVISIBLE);
            }
        });


        /////Move & decoration///////////////////

        if (textArray[0] != null) {

            textArray[0].setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    if (gestureDetector.onTouchEvent(event)) {

                        vibrator.vibrate(10);
                        if (stylingConst.isShown()) {
                            stylingConst.setVisibility(View.INVISIBLE);
                            textArray[0].setBackground(null);
                            itemConst.setVisibility(View.VISIBLE);
                        } else {
                            stylingConst.setVisibility(View.VISIBLE);
                            textArray[0].setBackground(getDrawable(R.drawable.highlight));
                            textArray[1].setBackground(null);
                            textArray[2].setBackground(null);
                            textArray[3].setBackground(null);
                            textArray[4].setBackground(null);
                            itemConst.setVisibility(View.INVISIBLE);
                            currentText = textArray[0];
                            sizeButton.setText("Size: "+currentText.getTextSize());
                            rotateButton.setText("Angle: "+currentText.getRotation());
                        }

                        return true;
                    } else {

                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                x_cord = v.getX() - event.getRawX();
                                y_cord = v.getY() - event.getRawY();
                                break;

                            case MotionEvent.ACTION_MOVE:
                                v.animate()
                                        .x(event.getRawX() + x_cord)
                                        .y(event.getRawY() + y_cord)
                                        .setDuration(0)
                                        .start();
                                break;

                            default:
                                return false;
                        }
                        return false;
                    }
                }
            });

        }

        if (textArray[1] != null) {

            textArray[1].setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (gestureDetector.onTouchEvent(event)) {

                        vibrator.vibrate(10);
                        if (stylingConst.isShown()) {
                            stylingConst.setVisibility(View.INVISIBLE);
                            textArray[1].setBackground(null);
                            itemConst.setVisibility(View.VISIBLE);
                        } else {
                            stylingConst.setVisibility(View.VISIBLE);
                            textArray[1].setBackground(getDrawable(R.drawable.highlight));
                            textArray[0].setBackground(null);
                            textArray[2].setBackground(null);
                            textArray[3].setBackground(null);
                            textArray[4].setBackground(null);
                            itemConst.setVisibility(View.INVISIBLE);
                            currentText = textArray[1];
                            sizeButton.setText("Size: "+currentText.getTextSize());
                            rotateButton.setText("Angle: "+currentText.getRotation());
                        }

                        return true;
                    } else {

                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                x_cord = v.getX() - event.getRawX();
                                y_cord = v.getY() - event.getRawY();
                                break;

                            case MotionEvent.ACTION_MOVE:
                                v.animate()
                                        .x(event.getRawX() + x_cord)
                                        .y(event.getRawY() + y_cord)
                                        .setDuration(0)
                                        .start();
                                break;

                            default:
                                return false;
                        }
                        return false;
                    }
                }
            });

        }


        if (textArray[2] != null) {

            textArray[2].setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (gestureDetector.onTouchEvent(event)) {

                        vibrator.vibrate(10);
                        if (stylingConst.isShown()) {
                            stylingConst.setVisibility(View.INVISIBLE);
                            textArray[2].setBackground(null);
                            itemConst.setVisibility(View.VISIBLE);
                        } else {
                            stylingConst.setVisibility(View.VISIBLE);
                            textArray[2].setBackground(getDrawable(R.drawable.highlight));
                            textArray[0].setBackground(null);
                            textArray[1].setBackground(null);
                            textArray[3].setBackground(null);
                            textArray[4].setBackground(null);
                            itemConst.setVisibility(View.INVISIBLE);
                            currentText = textArray[2];
                            sizeButton.setText("Size: "+currentText.getTextSize());
                            rotateButton.setText("Angle: "+currentText.getRotation());
                        }

                        return true;
                    } else {

                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                x_cord = v.getX() - event.getRawX();
                                y_cord = v.getY() - event.getRawY();
                                break;

                            case MotionEvent.ACTION_MOVE:
                                v.animate()
                                        .x(event.getRawX() + x_cord)
                                        .y(event.getRawY() + y_cord)
                                        .setDuration(0)
                                        .start();
                                break;

                            default:
                                return false;
                        }
                        return false;
                    }
                }
            });

        }


        if (textArray[3] != null) {


            textArray[3].setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (gestureDetector.onTouchEvent(event)) {

                        vibrator.vibrate(10);
                        if (stylingConst.isShown()) {
                            stylingConst.setVisibility(View.INVISIBLE);
                            textArray[3].setBackground(null);
                            itemConst.setVisibility(View.VISIBLE);
                        } else {
                            stylingConst.setVisibility(View.VISIBLE);
                            textArray[3].setBackground(getDrawable(R.drawable.highlight));
                            textArray[0].setBackground(null);
                            textArray[1].setBackground(null);
                            textArray[2].setBackground(null);
                            textArray[4].setBackground(null);
                            itemConst.setVisibility(View.INVISIBLE);
                            currentText = textArray[3];
                            sizeButton.setText("Size: "+currentText.getTextSize());
                            rotateButton.setText("Angle: "+currentText.getRotation());
                        }

                        return true;
                    } else {

                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                x_cord = v.getX() - event.getRawX();
                                y_cord = v.getY() - event.getRawY();
                                break;

                            case MotionEvent.ACTION_MOVE:
                                v.animate()
                                        .x(event.getRawX() + x_cord)
                                        .y(event.getRawY() + y_cord)
                                        .setDuration(0)
                                        .start();
                                break;

                            default:
                                return false;
                        }
                        return false;
                    }
                }
            });

        }

        if (textArray[4] != null) {

            textArray[4].setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (gestureDetector.onTouchEvent(event)) {

                        vibrator.vibrate(10);
                        if (stylingConst.isShown()) {
                            stylingConst.setVisibility(View.INVISIBLE);
                            textArray[4].setBackground(null);
                            itemConst.setVisibility(View.VISIBLE);
                        } else {
                            stylingConst.setVisibility(View.VISIBLE);
                            textArray[4].setBackground(getDrawable(R.drawable.highlight));
                            textArray[0].setBackground(null);
                            textArray[1].setBackground(null);
                            textArray[2].setBackground(null);
                            textArray[3].setBackground(null);
                            itemConst.setVisibility(View.INVISIBLE);
                            currentText = textArray[4];
                            sizeButton.setText("Size: "+currentText.getTextSize());
                            rotateButton.setText("Angle: "+currentText.getRotation());
                        }

                        return true;
                    } else {

                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                x_cord = v.getX() - event.getRawX();
                                y_cord = v.getY() - event.getRawY();
                                break;

                            case MotionEvent.ACTION_MOVE:
                                v.animate()
                                        .x(event.getRawX() + x_cord)
                                        .y(event.getRawY() + y_cord)
                                        .setDuration(0)
                                        .start();
                                break;

                            default:
                                return false;
                        }
                        return false;
                    }
                }
            });

        }

        /////Move & decoration end///////////////////

        //Edit text
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seekBarRotate.setVisibility(View.INVISIBLE);
                seekBarSize.setVisibility(View.INVISIBLE);
                editTextMethod(currentText.getText().toString());

            }
        });

        //Resize text
        sizeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (seekBarSize.isShown()) {
                    seekBarSize.setVisibility(View.INVISIBLE);
                } else {
                    seekBarSize.setVisibility(View.VISIBLE);
                    seekBarRotate.setVisibility(View.INVISIBLE);
                    changeTextSize((int) currentText.getTextSize());
                }
            }
        });

        //Rotate text
        rotateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (seekBarRotate.isShown()) {
                    seekBarRotate.setVisibility(View.INVISIBLE);
                } else {
                    seekBarSize.setVisibility(View.INVISIBLE);
                    seekBarRotate.setVisibility(View.VISIBLE);
                    rotatTextView((int) currentText.getRotation());
                }
            }
        });

        //Change color
        colorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seekBarRotate.setVisibility(View.INVISIBLE);
                seekBarSize.setVisibility(View.INVISIBLE);
                openColorPicker();
            }
        });

        //Font changer
        fontButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seekBarRotate.setVisibility(View.INVISIBLE);
                seekBarSize.setVisibility(View.INVISIBLE);
                openFontChanger(currentText.getText().toString());
            }
        });

    }

    public Bitmap takeScreenshot() {
        imageConst.setDrawingCacheEnabled(true);
        imageConst.buildDrawingCache(true);
        Bitmap b = Bitmap.createBitmap(imageConst.getDrawingCache());
        imageConst.setDrawingCacheEnabled(false);
        return b;
    }

    public void askName(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final EditText name = new EditText(this);
        name.setGravity(Gravity.CENTER);
        builder.setView(name);
        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Bitmap bitmap = takeScreenshot();
                saveBitmap(bitmap, name.getText().toString());
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.show();
    }


    public void saveBitmap(Bitmap bitmap, String name) {
        String fileName = name + ".png";
        File path = new File(Environment.getExternalStorageDirectory() +"/" + fileName);
        FileOutputStream fileOutputStream;
        try {
            fileOutputStream = new FileOutputStream(path);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Toast.makeText(this, "Saved in Gallery", Toast.LENGTH_SHORT).show();
        Uri uri = Uri.fromFile(path);
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("image/png");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "");
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(Intent.createChooser(shareIntent, "Share Image"));

    }

    private void openColorPicker() {

        AmbilWarnaDialog colorPicker = new AmbilWarnaDialog(this, currentText.getCurrentTextColor(), new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {

            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {

                currentText.setTextColor(color);
            }
        });
        colorPicker.show();

    }

    private void editTextMethod(String text) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        int editId = 0, leftButtonId = 1, centerButtonId = 2, rightButtonId = 3, parent = 4;

        ConstraintLayout editConstraint = new ConstraintLayout(this);
        ConstraintLayout.LayoutParams editConstParams = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_CONSTRAINT);
        editConstraint.setMinHeight(300);
        editConstraint.setId(parent);
        editConstraint.setLayoutParams(editConstParams);

        final EditText dEditText = new EditText(this);
        ConstraintLayout.LayoutParams dEditTextParams = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
        dEditText.setLayoutParams(dEditTextParams);
        dEditText.setId(editId);
        //dEditText.setGravity(Gravity.CENTER);
        dEditText.setText(text);

        ConstraintLayout.LayoutParams leftButtonParams = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
        Button leftButton = new Button(this);
        leftButton.setLayoutParams(leftButtonParams);
        leftButton.setBackground(getResources().getDrawable(R.drawable.ic_align_left));
        leftButton.setId(leftButtonId);

        Button centerButton = new Button(this);
        ConstraintLayout.LayoutParams centreButtonParams = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
        centerButton.setLayoutParams(centreButtonParams);
        centerButton.setBackground(getResources().getDrawable(R.drawable.ic_align_center));
        centerButton.setId(centerButtonId);

        Button rightButton = new Button(this);
        ConstraintLayout.LayoutParams rightButtonParams = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
        rightButton.setLayoutParams(rightButtonParams);
        rightButton.setBackground(getResources().getDrawable(R.drawable.ic_align_right));
        rightButton.setId(rightButtonId);


        editConstraint.addView(dEditText);
        editConstraint.addView(leftButton);
        editConstraint.addView(centerButton);
        editConstraint.addView(rightButton);

        ConstraintSet editSet = new ConstraintSet();
        editSet.clone(editConstraint);

        editSet.connect(dEditText.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP);
        editSet.connect(dEditText.getId(), ConstraintSet.BOTTOM, centerButton.getId(), ConstraintSet.TOP);

        editSet.connect(leftButton.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
        editSet.connect(leftButton.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM);

        editSet.connect(centerButton.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
        editSet.connect(centerButton.getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END);
        editSet.connect(centerButton.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM);

        editSet.connect(rightButton.getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END);
        editSet.connect(rightButton.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM);

        editSet.applyTo(editConstraint);

        builder.setView(editConstraint);
        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                currentText.setText(dEditText.getText());
                currentText.setGravity(dEditText.getGravity());

            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dEditText.setGravity(Gravity.START);
            }
        });

        centerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dEditText.setGravity(Gravity.CENTER);
            }
        });

        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dEditText.setGravity(Gravity.END);
            }
        });

        builder.show();

    }

    private void rotatTextView(int rotation) {
        seekBarRotate.setMax(360);
        seekBarRotate.setProgress(rotation);

        seekBarRotate.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                currentText.setRotation(progress);
                rotateButton.setText("Angle: "+progress);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void changeTextSize(int size) {
        seekBarSize.setMax(100);
        seekBarSize.setProgress(size);

        seekBarSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                currentText.setTextSize(TypedValue.COMPLEX_UNIT_PX, progress);
                sizeButton.setText("Size: "+progress);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    private void createTextBuilder() {
        if (id < 5) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            int editId = 0, leftButtonId = 1, centerButtonId = 2, rightButtonId = 3, parent = 4;

            ConstraintLayout editConstraint = new ConstraintLayout(this);
            ConstraintLayout.LayoutParams editConstParams = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_CONSTRAINT);
            editConstraint.setMinHeight(300);
            editConstraint.setId(parent);
            editConstraint.setLayoutParams(editConstParams);

            final EditText dEditText = new EditText(this);
            ConstraintLayout.LayoutParams dEditTextParams = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
            dEditText.setLayoutParams(dEditTextParams);
            dEditText.setId(editId);
            dEditText.setGravity(Gravity.CENTER);
            dEditText.setText("Sample text");

            ConstraintLayout.LayoutParams leftButtonParams = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
            Button leftButton = new Button(this);
            leftButton.setLayoutParams(leftButtonParams);
            leftButton.setBackground(getResources().getDrawable(R.drawable.ic_align_left));
            leftButton.setId(leftButtonId);

            Button centerButton = new Button(this);
            ConstraintLayout.LayoutParams centreButtonParams = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
            centerButton.setLayoutParams(centreButtonParams);
            centerButton.setBackground(getResources().getDrawable(R.drawable.ic_align_center));
            centerButton.setId(centerButtonId);

            Button rightButton = new Button(this);
            ConstraintLayout.LayoutParams rightButtonParams = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
            rightButton.setLayoutParams(rightButtonParams);
            rightButton.setBackground(getResources().getDrawable(R.drawable.ic_align_right));
            rightButton.setId(rightButtonId);


            editConstraint.addView(dEditText);
            editConstraint.addView(leftButton);
            editConstraint.addView(centerButton);
            editConstraint.addView(rightButton);

            ConstraintSet editSet = new ConstraintSet();
            editSet.clone(editConstraint);

            editSet.connect(dEditText.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP);
            editSet.connect(dEditText.getId(), ConstraintSet.BOTTOM, centerButton.getId(), ConstraintSet.TOP);

            editSet.connect(leftButton.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
            editSet.connect(leftButton.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM);

            editSet.connect(centerButton.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
            editSet.connect(centerButton.getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END);
            editSet.connect(centerButton.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM);

            editSet.connect(rightButton.getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END);
            editSet.connect(rightButton.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM);

            editSet.applyTo(editConstraint);

            builder.setView(editConstraint);
            builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    createText(dEditText.getText().toString(), dEditText.getGravity());
                }
            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            leftButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dEditText.setGravity(Gravity.START);
                }
            });

            centerButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dEditText.setGravity(Gravity.CENTER);
                }
            });

            rightButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dEditText.setGravity(Gravity.END);
                }
            });

            builder.show();

        } else {
            Toast.makeText(this, "Maximum 5 Texts you can use", Toast.LENGTH_SHORT).show();
        }

    }

    public void createText(String text, Integer gravity) {

        ConstraintSet set = new ConstraintSet();
        set.clone(imageConst);
        //New textview
        textArray[id].setText(text);
        textArray[id].setId(id);
        textArray[id].setClickable(true);
        textArray[id].setPadding(8, 8, 8, 8);
        textArray[id].setGravity(gravity);
        imageConst.addView(textArray[id]);
        set.connect(textArray[id].getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0);
        set.connect(textArray[id].getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0);
        set.connect(textArray[id].getId(), ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT, 0);
        set.connect(textArray[id].getId(), ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT, 0);
        set.constrainHeight(textArray[id].getId(), ConstraintSet.WRAP_CONTENT);
        set.constrainWidth(textArray[id].getId(), ConstraintSet.WRAP_CONTENT);
        set.applyTo(imageConst);
        id = id + 1;
    }

    public void openGallery() {
        startActivityForResult(new Intent()
                .setAction(Intent.ACTION_GET_CONTENT)
                .setType("image/*"), GALLERY_PICK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            final Uri imageUri = data.getData();
            imageView.setImageURI(imageUri);
            recyclerView.setVisibility(View.INVISIBLE);
            itemConst.setVisibility(View.VISIBLE);
        }
    }

    public void selectImage(int i) {
        recyclerView.setVisibility(View.INVISIBLE);
        itemConst.setVisibility(View.VISIBLE);
        imageView.setImageDrawable(imageList.get(i));
    }

    private void loadImages() {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        linearLayoutManager.setStackFromEnd(false);
        recyclerView.setLayoutManager(linearLayoutManager);

        ImageAdapter imageAdapter = new ImageAdapter(this, imageList);
        recyclerView.setAdapter(imageAdapter);
    }


    //////////////////////////////////////////////////////////////////////////////////

    private void makeImageList() {
        imageList = new ArrayList<>();
        AssetManager assetManager = getAssets();
        InputStream inputStream = null;

        for (int i = 1; i < 7; i++) {

            try {
                inputStream = assetManager.open("images/image" + i + ".png");
                imageList.add(Drawable.createFromStream(inputStream, ""));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    /////////////////////////////////////////////////////

    private void makeFontList() {
        fontList = new ArrayList<>();
        fontNamesList = new ArrayList<>();
        AssetManager assetManager = this.getAssets();

        try {
            String[] files = assetManager.list("fonts");
            fontNamesList = new LinkedList<>(Arrays.asList(files));
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < fontNamesList.size(); i++){
            fontList.add(Typeface.createFromAsset(getAssets(), "fonts/"+fontNamesList.get(i)));

        }
    }

    private void openFontChanger(String text) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);


        ConstraintLayout fontConst = new ConstraintLayout(this);
        ConstraintLayout.LayoutParams fontConstParams = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
        fontConst.setLayoutParams(fontConstParams);
        fontConst.setId(000);

        fontTextView = new TextView(this);
        ConstraintLayout.LayoutParams fontTextViewParams = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
        fontTextView.setLayoutParams(fontTextViewParams);
        fontTextView.setId(00);
        fontTextView.setMaxLines(1);
        fontTextView.setText(text);
        fontTextView.setGravity(Gravity.CENTER);
        fontTextView.setTextColor(getResources().getColor(R.color.colorWhite));
        fontTextView.setBackgroundColor(getResources().getColor(R.color.colorBlack));
        fontTextView.setTextSize(48);

        RecyclerView fontRecycler = new RecyclerView(this);
        ConstraintLayout.LayoutParams fontRecyclerParams = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
        fontRecycler.setLayoutParams(fontRecyclerParams);
        fontRecycler.setId(0);

        fontConst.addView(fontTextView);
        fontConst.addView(fontRecycler);

        ConstraintSet fontSet = new ConstraintSet();
        fontSet.clone(fontConst);
        fontSet.connect(fontTextView.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP);

        fontSet.connect(fontRecycler.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
        fontSet.connect(fontRecycler.getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END);
        fontSet.connect(fontRecycler.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM);

        fontSet.applyTo(fontConst);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        fontRecycler.setLayoutManager(linearLayoutManager);

        FontAdapter fontAdapter = new FontAdapter(this, fontList, fontNamesList);
        fontRecycler.setAdapter(fontAdapter);

        builder.setView(fontConst);
        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                currentText.setTypeface(fontList.get(position));
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.show();
    }

    public void updateBroadCast(int i){
        ////Broadcaster
        BroadcastReceiver mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                position = intent.getIntExtra("position", 0);
            }
        };
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, new IntentFilter("intent"));
        //////////////////till here//////////
        fontTextView.setTypeface(fontList.get(i));
    }


    private void donateDev() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Want to Donate?")
                .setMessage("Buy a coffee for me,\n" +
                        "It shows me your love and helps to keep development active and keep it ad free")
                .setPositiveButton("Donate", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://paypal.me/encrustace?locale.x=en_GB"));
                        startActivity(browserIntent);
                    }
                }).setNegativeButton("Later", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void askPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        1);
            }

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Permission Granted
                } else {
                    //Not  granted
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setNegativeButton("No", null)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Home.super.onBackPressed();
                    }
                })
                .show();
    }
}
