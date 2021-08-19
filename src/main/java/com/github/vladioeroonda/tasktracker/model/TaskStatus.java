package com.github.vladioeroonda.tasktracker.model;

import java.io.Serializable;

public enum TaskStatus implements Serializable {
    BACKLOG, IN_PROGRESS, DONE, CANCELLED;
}
