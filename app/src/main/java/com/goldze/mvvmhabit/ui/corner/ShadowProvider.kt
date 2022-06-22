package com.goldze.mvvmhabit.ui.corner

interface ShadowProvider {
    fun getCornerRadius(): Float
    fun getStrokeWidth(): Float
    fun getStrokeColor(): Int
    fun getInitialTranslationZ(): Float
}