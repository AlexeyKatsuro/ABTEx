/*
 * Copyright 2018 LWO LLC
 */

package com.e.btex.di.scope

import javax.inject.Scope

/**
 * The FragmentScoped custom scoping annotation specifies that the lifespan of a dependency be
 * the same as that of a Fragment. This is used to annotate dependencies that behave like a
 * singleton within the lifespan of a Fragment
 */
@Scope
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
annotation class FragmentScoped
