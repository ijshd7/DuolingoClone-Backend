package com.testingpractice.duoclonebackend.dto;

public record QuestResponse (
        String code,
        Integer progress,
        Integer total,
        boolean active
) {

}
