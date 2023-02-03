package com.birariro.playday.adapter.event.registration;

import com.birariro.playday.adapter.event.BaseEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class NewRegistrationEvent extends BaseEvent {
    private String email;
}