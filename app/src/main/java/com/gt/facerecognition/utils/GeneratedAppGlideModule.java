package com.gt.facerecognition.utils;

import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;

/**
 * Failed to find GeneratedAppGlideModule. You should include an annotationProcessor compile dependency
 * 为解决这个问题创建该类
 */
@GlideModule
public class GeneratedAppGlideModule extends AppGlideModule {
    @Override
    public boolean isManifestParsingEnabled() {
        return false;
    }
}

