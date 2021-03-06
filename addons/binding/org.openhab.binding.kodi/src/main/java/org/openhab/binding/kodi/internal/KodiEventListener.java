/**
 * Copyright (c) 2014-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.kodi.internal;

import java.util.EventListener;

import org.openhab.binding.kodi.internal.protocol.KodiConnection;

/**
 * Interface which has to be implemented by a class in order to get status updates from a {@link KodiConnection}
 *
 * @author Paul Frank
 *
 */
public interface KodiEventListener extends EventListener {
    public enum KodiState {
        Play,
        Pause,
        End,
        Stop,
        Rewind,
        FastForward
    }

    void updateConnectionState(boolean connected);

    void updateScreenSaverState(boolean screenSaveActive);

    void updateVolume(int volume);

    void updatePlayerState(KodiState state);

    void updateMuted(boolean muted);

    void updateTitle(String title);

    void updateAlbum(String album);

    void updateArtist(String artist);

    void updateMediaType(String mediaType);
}