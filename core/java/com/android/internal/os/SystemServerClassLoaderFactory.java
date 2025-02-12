/*
 * Copyright (C) 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.internal.os;

import android.os.Build;
import android.util.ArrayMap;

import dalvik.system.PathClassLoader;

/** @hide */
public final class SystemServerClassLoaderFactory {
    /**
     * Map of paths to PathClassLoader for standalone system server jars.
     */
    private static final ArrayMap<String, PathClassLoader> sLoadedPaths = new ArrayMap<>();

    /**
     * Creates and caches a ClassLoader for the jar at the given path, or returns a cached
     * ClassLoader if it exists.
     *
     * The parent class loader should always be the system server class loader. Changing it has
     * implications that require discussion with the mainline team.
     *
     * @hide for internal use only
     */
    public static PathClassLoader getOrCreateClassLoader(String path, ClassLoader parent) {
        PathClassLoader pathClassLoader = sLoadedPaths.get(path);
        if (pathClassLoader == null) {
            pathClassLoader = (PathClassLoader) ClassLoaderFactory.createClassLoader(
                    path, /*librarySearchPath=*/null, /*libraryPermittedPath=*/null, parent,
                    Build.VERSION.SDK_INT, /*isNamespaceShared=*/true , /*classLoaderName=*/null);
            sLoadedPaths.put(path, pathClassLoader);
        }
        return pathClassLoader;
    }
}
