package com.ada.marcin.screen.ui;

import com.ada.marcin.model.ShipEvent;

public interface Observer {

    void notify(ShipEvent shipEvent,
                String data);
}
