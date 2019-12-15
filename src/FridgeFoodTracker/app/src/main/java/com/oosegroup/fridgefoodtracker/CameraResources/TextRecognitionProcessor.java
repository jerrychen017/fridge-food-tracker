package com.oosegroup.fridgefoodtracker.CameraResources;

// Copyright 2018 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;
import com.oosegroup.fridgefoodtracker.Activities.CameraActivity;
import com.oosegroup.fridgefoodtracker.Activities.MainActivity;
import com.oosegroup.fridgefoodtracker.models.ItemListController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Processor for the text recognition demo.
 */
public class TextRecognitionProcessor extends VisionProcessorBase<FirebaseVisionText> {

    private static final String TAG = "TextRecProc";

    private final FirebaseVisionTextRecognizer detector;
    public CameraActivity activity;

    private static HashMap<String, Integer> itemsDict = new HashMap<>();

    public TextRecognitionProcessor() {
        detector = FirebaseVision.getInstance().getOnDeviceTextRecognizer();
    }

    @Override
    public void stop() {
        try {
            Log.d("OCR", "STOP TEXT RECOGNITION");
            detector.close();
        } catch (IOException e) {
            Log.e(TAG, "Exception thrown while trying to close Text Detector: " + e);
        }
    }

    @Override
    protected Task<FirebaseVisionText> detectInImage(FirebaseVisionImage image) {
        return detector.processImage(image);
    }

    @Override
    protected void onSuccess(
            @Nullable Bitmap originalCameraImage,
            @NonNull FirebaseVisionText results,
            @NonNull FrameMetadata frameMetadata,
            @NonNull GraphicOverlay graphicOverlay) {
        graphicOverlay.clear();
        Log.d("onSuccess", "onSuccess: IN SUCCESS");
        if (originalCameraImage != null) {
            CameraImageGraphic imageGraphic = new CameraImageGraphic(graphicOverlay,
                    originalCameraImage);
            graphicOverlay.add(imageGraphic);
        }
        Log.d("firebase","RESULT GET TEXT: " + results.getText());
        List<FirebaseVisionText.TextBlock> blocks = results.getTextBlocks();
        for (int i = 0; i < blocks.size(); i++) {
            List<FirebaseVisionText.Line> lines = blocks.get(i).getLines();
            Log.d("firebase","GET BLOCK: " + i);
            for (int j = 0; j < lines.size(); j++) {
                List<FirebaseVisionText.Element> elements = lines.get(j).getElements();
                String lineText = lines.get(j).getText();

                Log.d("firebase", "ADD TO DICT: " + lineText);
                if(isAllUpper(lineText) && !itemsDict.containsKey(lineText) ) {
                    itemsDict.put(lineText, 1);
                } else if (isAllUpper(lineText) && itemsDict.containsKey(lineText)) {
                    itemsDict.put(lineText, itemsDict.get(lineText) + 1);
                }
                for (int k = 0; k < elements.size(); k++) {
                    GraphicOverlay.Graphic textGraphic = new TextGraphic(graphicOverlay,
                            elements.get(k));
                    graphicOverlay.add(textGraphic);
                }
            }
        }
        graphicOverlay.postInvalidate();

        Log.d("printing", "dict: " + printDict(itemsDict));
    }
    private String printDict (Map<String, Integer> map) {
        String fin = "";
        for(Map.Entry<String, Integer> e : map.entrySet())
        {
            fin = fin + e.getKey()+": "+e.getValue() + "\n";
        }
        return fin;
    }

    //parses strings and recognizes items as all capital letters
    //ignores digits and spaces for more accurate detection
    private boolean isAllUpper(String str) {

        for(char c : str.toCharArray()) {
            if(c == ' ') {
                continue;
            }
            if(Character.isLetter(c) && Character.isLowerCase(c)) {
                return false;
            } else if(!Character.isLetter(c)) {
                return false;
            }
        }
        return true;
    }

    public HashMap<String, Integer> getItemsDict() {
        return itemsDict;
    }

    @Override
    protected void onFailure(@NonNull Exception e) {
        Log.w(TAG, "Text detection failed." + e);
    }
}