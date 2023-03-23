package com.example.test.util;

import lombok.Getter;

/**
 * Список состояний отчета
 */
@Getter
public abstract class StateReport {

  public final static String NOT_STARTED = "NOT_STARTED";
  public final static String WAIT_ID_PET_REPORT = "WAIT_ID_PET_REPORT";
  public final static String WAIT_PHOTO_REPORT = "WAIT_PHOTO_REPORT";
  public final static String WAIT_DIET_REPORT = "WAIT_DIET_REPORT";
  public final static String WAIT_FEELINGS_REPORT = "WAIT_HEALTH_REPORT";
  public final static String WAIT_HABITS_REPORT = "WAIT_HABITS_REPORT";
  public final static String WAIT_CLICK_SEND_REPORT = "WAIT_CLICK_SEND_REPORT";
  public final static String FINISHED = "FINISHED";


}
