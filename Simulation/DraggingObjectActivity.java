package com.example.team1min.Simulation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MotionEventCompat;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.example.team1min.R;

public class DraggingObjectActivity extends AppCompatActivity {
    ImageView imageView;
    ImageView imageView2;
    float xDown=0, yDown=0;
    float xDown2=0, yDown2=0;

    @SuppressLint("ClickableViewAccessibility")

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draging_object);

        imageView = findViewById(R.id.image_view);
        imageView2 = findViewById(R.id.image_view2);
        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                switch (event.getActionMasked()){
                    case MotionEvent.ACTION_DOWN:
                        xDown = event.getX();
                        yDown = event.getY();
                        break;
                        /*final int pointerIndex = MotionEventCompat.getActionIndex(event);
                        final float x = MotionEventCompat.getX(event, pointerIndex);
                        final float y = MotionEventCompat.getY(event, pointerIndex);

                        // Remember where we started (for dragging)
                        mLastTouchX = x;
                        mLastTouchY = y;
                        // Save the ID of this pointer (for dragging)
                        mActivePointerId = MotionEventCompat.getPointerId(ev, 0);*/

                        case MotionEvent.ACTION_MOVE:
                            float movedX, movedY;
                            movedX = event.getX();
                            movedY = event.getY();

                            float distanceX = movedX-xDown;
                            float distanceY = movedY-yDown;

                            imageView.setX(imageView.getX()+distanceX);
                            imageView.setY(imageView.getY()+distanceY);



                           /* xDown = movedX;
                            yDown = movedY;*/

                            break;

                }
                return true;
            }
        });

        imageView2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                switch (event.getActionMasked()){
                    case MotionEvent.ACTION_DOWN:
                        xDown2 = event.getX();
                        yDown2 = event.getY();
                        break;
                        /*final int pointerIndex = MotionEventCompat.getActionIndex(event);
                        final float x = MotionEventCompat.getX(event, pointerIndex);
                        final float y = MotionEventCompat.getY(event, pointerIndex);

                        // Remember where we started (for dragging)
                        mLastTouchX = x;
                        mLastTouchY = y;
                        // Save the ID of this pointer (for dragging)
                        mActivePointerId = MotionEventCompat.getPointerId(ev, 0);*/

                    case MotionEvent.ACTION_MOVE:
                        float movedX, movedY;
                        movedX = event.getX();
                        movedY = event.getY();

                        float distanceX = movedX-xDown;
                        float distanceY = movedY-yDown;

                        imageView2.setX(imageView2.getX()+distanceX);
                        imageView2.setY(imageView2.getY()+distanceY);



                           /* xDown = movedX;
                            yDown = movedY;*/

                        break;

                }
                return true;
            }
        });

    }
}