/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import java.io.File;
import java.util.function.Supplier;

import com.badlog.lib.BadLog;
import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.Timer;

/**
 * Add your docs here.
 */
public class Log {

  private static final int kMaxNumFiles = 971;
  private static final String kDefaultPath = "/home/lvuser/logs/";
  private static final String kDefaultSimPath = "\\logs\\";

  public static BadLog log;
  private static Notifier logger;

  public static void init(String directory, String filename, RobotContainer robotContainer) {
    
    log = BadLog.init(getDesiredFile(directory, filename));
    createTopic("Time", () -> Timer.getFPGATimestamp());

    createTopic("RightMaster" + "/Voltage", () -> robotContainer.drivetrain.rightMaster.getMotorOutputVoltage());
    createTopic("LeftMaster" + "/Voltage", () -> robotContainer.drivetrain.leftMaster.getMotorOutputVoltage());
    createTopic("RightFollower" + "/Voltage", () -> robotContainer.drivetrain.rightSlave.getMotorOutputVoltage());
    createTopic("LeftFollower" + "/Voltage", () -> robotContainer.drivetrain.leftSlave.getMotorOutputVoltage());
    createTopic("ClimberSensorVelocity" + "/Velocity", () -> robotContainer.everybotClimber.climberMaster.getSelectedSensorVelocity());
    createTopic("ClimberTrajVelocity" + "/Velocity", () -> robotContainer.everybotClimber.climberMaster.getActiveTrajectoryVelocity());
    createTopic("ClimberSensorPosition" + "/Position", () -> robotContainer.everybotClimber.climberMaster.getSelectedSensorPosition());
    createTopic("ClimberTrajPosition" + "/Position", () -> robotContainer.everybotClimber.climberMaster.getActiveTrajectoryPosition());
    createTopic("ClimberOutputVoltage" + "/Voltage", () -> robotContainer.everybotClimber.climberMaster.getMotorOutputVoltage());
    createTopic("Elevator Position" + "/Position", () -> robotContainer.elevator.elevator.getSelectedSensorPosition());
    createTopic("Elevator Voltage " + "/Voltage", () -> robotContainer.elevator.elevator.getMotorOutputVoltage());
    createTopic("Arm Position" + "/Position", () -> robotContainer.armTrapezoid.arm.getSelectedSensorPosition());
    createTopic("Arm Voltage" + "/Voltage", () -> robotContainer.armTrapezoid.arm.getMotorOutputVoltage());
    
    log.finishInitialization();
  }

  public static void initAndLog(String directory, String filename, double period, RobotContainer robotContainer) {
    init(directory, filename, robotContainer);
    logger = new Notifier(() -> {
      Log.log();
    });
    logger.startPeriodic(period);
  }

  private static String getDesiredFile(String directory, String filename) {
    // Check that the directory exists
    String pathToUse;
    File dir = new File(directory);
    if (dir.getParentFile().isDirectory()) {
      if (!dir.isDirectory()) {
        dir.mkdir();
      }

      pathToUse = directory;
    } else {
      if (RobotBase.isReal()) {
        File defaultLogsFolder = new File(kDefaultPath);

        if (!(defaultLogsFolder.isDirectory())) {
          defaultLogsFolder.mkdir();
        }

        pathToUse = kDefaultPath;

      } else {
        pathToUse = System.getProperty("user.dir") + kDefaultSimPath;
      }
    }

    int fileNumber = 0;

    for (int i = 0; i < kMaxNumFiles; i++) {
      File file = (new File(pathToUse + filename + String.valueOf(i) + ".csv"));

      if (!file.exists() && !file.isDirectory()) {
        fileNumber = i;
        break;
      }
    }
    return pathToUse + filename + String.valueOf(fileNumber) + ".csv";
  }

  public static void log() {
    log.updateTopics();
    log.log();
  }

  public static void createTopic(String name, Supplier<Double> toLog) {
    BadLog.createTopic(name, "ul", toLog);
  }

  public static void createTopicStr(String name, Supplier<String> toLog) {
    BadLog.createTopicStr(name, "ul", toLog);
  }
}