/*
 *  Copyright 2016 Jeroen Mols
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package cn.ac.ict.alpha.activities.face.recorder;

import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.media.MediaRecorder.OnInfoListener;
import android.view.Surface;
import android.view.SurfaceHolder;

import java.io.IOException;

import cn.ac.ict.alpha.activities.face.CLog;
import cn.ac.ict.alpha.activities.face.VideoFile;
import cn.ac.ict.alpha.activities.face.camera.CameraWrapper;
import cn.ac.ict.alpha.activities.face.camera.OpenCameraException;
import cn.ac.ict.alpha.activities.face.camera.PrepareCameraException;
import cn.ac.ict.alpha.activities.face.camera.RecordingSize;
import cn.ac.ict.alpha.activities.face.configuration.CaptureConfiguration;
import cn.ac.ict.alpha.activities.face.preview.CapturePreview;
import cn.ac.ict.alpha.activities.face.preview.CapturePreviewInterface;

public class VideoRecorder implements OnInfoListener, CapturePreviewInterface {

    private CameraWrapper mCameraWrapper;
    private final Surface        mPreviewSurface;
    private CapturePreview mVideoCapturePreview;

    private final CaptureConfiguration mCaptureConfiguration;
    private final VideoFile mVideoFile;

    private MediaRecorder mRecorder;
    private boolean mRecording = false;
    private final VideoRecorderInterface mRecorderInterface;

    public VideoRecorder(VideoRecorderInterface recorderInterface, CaptureConfiguration captureConfiguration, VideoFile videoFile,
                         CameraWrapper cameraWrapper, SurfaceHolder previewHolder) {
        mCaptureConfiguration = captureConfiguration;
        mRecorderInterface = recorderInterface;
        mVideoFile = videoFile;
        mCameraWrapper = cameraWrapper;
        mPreviewSurface = previewHolder.getSurface();

        initializeCameraAndPreview(previewHolder);
    }

    protected void initializeCameraAndPreview(SurfaceHolder previewHolder) {
        try {
            mCameraWrapper.openCamera();
        } catch (final OpenCameraException e) {
            e.printStackTrace();
            mRecorderInterface.onRecordingFailed(e.getMessage());
            return;
        }

        mVideoCapturePreview = new CapturePreview(this, mCameraWrapper, previewHolder);
    }

    public void toggleRecording() throws AlreadyUsedException {
        if (mCameraWrapper == null) {
            throw new AlreadyUsedException();
        }

        if (isRecording()) {
            stopRecording(null);
        } else {
            startRecording();
        }
    }

    protected void startRecording() {
        mRecording = false;

        if (!initRecorder()) return;
        if (!prepareRecorder()) return;
        if (!startRecorder()) return;

        mRecording = true;
        mRecorderInterface.onRecordingStarted();
        CLog.d(CLog.RECORDER, "Successfully started recording - outputfile: " + mVideoFile.getFullPath());
    }

    public void stopRecording(String message) {
        if (!isRecording()) return;
        try {
            getMediaRecorder().stop();
            mRecorderInterface.onRecordingSuccess();
            CLog.d(CLog.RECORDER, "Successfully stopped recording - outputfile: " + mVideoFile.getFullPath());
        } catch (final RuntimeException e) {
            CLog.d(CLog.RECORDER, "Failed to stop recording");
        }

        mRecording = false;
        mRecorderInterface.onRecordingStopped(message);
    }

    private boolean initRecorder() {
        try {
            mCameraWrapper.prepareCameraForRecording();
        } catch (final PrepareCameraException e) {
            e.printStackTrace();
            mRecorderInterface.onRecordingFailed("Unable to record video");
            CLog.e(CLog.RECORDER, "Failed to initialize recorder - " + e.toString());
            return false;
        }

        setMediaRecorder(new MediaRecorder());
        configureMediaRecorder(getMediaRecorder(), mCameraWrapper.getCamera());

        CLog.d(CLog.RECORDER, "MediaRecorder successfully initialized");
        return true;
    }

    @SuppressWarnings("deprecation")
    protected void configureMediaRecorder(final MediaRecorder recorder, android.hardware.Camera camera) throws IllegalStateException, IllegalArgumentException {
        recorder.setCamera(camera);
        recorder.setAudioSource(mCaptureConfiguration.getAudioSource());
        recorder.setVideoSource(mCaptureConfiguration.getVideoSource());

        CamcorderProfile baseProfile = mCameraWrapper.getBaseRecordingProfile();
        baseProfile.fileFormat = mCaptureConfiguration.getOutputFormat();

        RecordingSize size = mCameraWrapper.getSupportedRecordingSize(mCaptureConfiguration.getVideoWidth(), mCaptureConfiguration.getVideoHeight());
        baseProfile.videoFrameWidth = size.width;
        baseProfile.videoFrameHeight = size.height;
        baseProfile.videoBitRate = mCaptureConfiguration.getVideoBitrate();

        baseProfile.audioCodec = mCaptureConfiguration.getAudioEncoder();
        baseProfile.videoCodec = mCaptureConfiguration.getVideoEncoder();

        recorder.setProfile(baseProfile);
        recorder.setMaxDuration(mCaptureConfiguration.getMaxCaptureDuration());
        recorder.setOutputFile(mVideoFile.getFullPath());
        recorder.setOrientationHint(mCameraWrapper.getRotationCorrection());

        try {
            recorder.setMaxFileSize(mCaptureConfiguration.getMaxCaptureFileSize());
        } catch (IllegalArgumentException e) {
            CLog.e(CLog.RECORDER, "Failed to set max filesize - illegal argument: " + mCaptureConfiguration.getMaxCaptureFileSize());
        } catch (RuntimeException e2) {
            CLog.e(CLog.RECORDER, "Failed to set max filesize - runtime exception");
        }
        recorder.setOnInfoListener(this);
    }

    private boolean prepareRecorder() {
        try {
            getMediaRecorder().prepare();
            CLog.d(CLog.RECORDER, "MediaRecorder successfully prepared");
            return true;
        } catch (final IllegalStateException e) {
            e.printStackTrace();
            CLog.e(CLog.RECORDER, "MediaRecorder preparation failed - " + e.toString());
            return false;
        } catch (final IOException e) {
            e.printStackTrace();
            CLog.e(CLog.RECORDER, "MediaRecorder preparation failed - " + e.toString());
            return false;
        }
    }

    private boolean startRecorder() {
        try {
            getMediaRecorder().start();
            CLog.d(CLog.RECORDER, "MediaRecorder successfully started");
            return true;
        } catch (final IllegalStateException e) {
            e.printStackTrace();
            CLog.e(CLog.RECORDER, "MediaRecorder start failed - " + e.toString());
            return false;
        } catch (final RuntimeException e2) {
            e2.printStackTrace();
            CLog.e(CLog.RECORDER, "MediaRecorder start failed - " + e2.toString());
            mRecorderInterface.onRecordingFailed("Unable to record video with given settings");
            return false;
        }
    }

    protected boolean isRecording() {
        return mRecording;
    }

    protected void setMediaRecorder(MediaRecorder recorder) {
        mRecorder = recorder;
    }

    protected MediaRecorder getMediaRecorder() {
        return mRecorder;
    }

    private void releaseRecorderResources() {
        CLog.d(CLog.RECORDER, "recorder release");
        MediaRecorder recorder = getMediaRecorder();
        CLog.d(CLog.RECORDER, "get recorder");
        if (recorder != null) {
            CLog.d(CLog.RECORDER, "release recorder");
            recorder.release();
            CLog.d(CLog.RECORDER, "set recorder = null");
            setMediaRecorder(null);
        }
    }

    public void releaseAllResources() {
        CLog.d(CLog.RECORDER, "start Released all resources");
        if (mVideoCapturePreview != null) {
            CLog.d(CLog.RECORDER, "mVideoCapturePreview release");
            mVideoCapturePreview.releasePreviewResources();
        }
        if (mCameraWrapper != null) {
            CLog.d(CLog.RECORDER, "mCameraWrapper release");
            mCameraWrapper.releaseCamera();
            mCameraWrapper = null;
        }
        releaseRecorderResources();
        CLog.d(CLog.RECORDER, "Released all resources");
    }

    @Override
    public void onCapturePreviewFailed() {
        mRecorderInterface.onRecordingFailed("Unable to show camera preview");
    }

    @Override
    public void onInfo(MediaRecorder mr, int what, int extra) {
        switch (what) {
            case MediaRecorder.MEDIA_RECORDER_INFO_UNKNOWN:
                // NOP
                break;
            case MediaRecorder.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED:
                CLog.d(CLog.RECORDER, "MediaRecorder max duration reached");
                stopRecording("Capture stopped - Max duration reached");
                break;
            case MediaRecorder.MEDIA_RECORDER_INFO_MAX_FILESIZE_REACHED:
                CLog.d(CLog.RECORDER, "MediaRecorder max filesize reached");
                stopRecording("Capture stopped - Max file size reached");
                break;
            default:
                break;
        }
    }

}