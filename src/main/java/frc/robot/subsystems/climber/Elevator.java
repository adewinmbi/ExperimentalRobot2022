package frc.robot.subsystems.climber;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants.ClimberConstants;

public class Elevator {
    public TalonSRX elevator = new TalonSRX(ClimberConstants.kElevatorTalonID);

    private static double elevatorKp = 1.0;
    private static double elevatorKd = 0.0002;
    private static double elevatorFF = 0;

    public Elevator() {
        elevator.setInverted(true);
    }
    
    public void moveElevator(double speed) {
        elevator.set(ControlMode.PercentOutput, speed);
    }

    public void moveElevatortoPos(double target, double errorThreshold) {
        double error = Math.abs(target - elevator.getSelectedSensorPosition());
        double oldError = Math.abs(target - elevator.getSelectedSensorPosition());
        double elevatorSpeed = 0;

        while (error > errorThreshold) {
            oldError = error;
            error = Math.abs(target - elevator.getSelectedSensorPosition());
            elevatorSpeed = (elevatorKp * error * 0.001) + (elevatorKd * (oldError - error) * 0.001 / 0.01) + (elevatorFF * 0.001);

            if (elevator.getSelectedSensorPosition() < target) {
                moveElevator(elevatorSpeed);
            } else if (elevator.getSelectedSensorPosition() > target) {
                moveElevator((elevatorSpeed * -1));
            }
        }

        SmartDashboard.putNumber(" Elevator Target ", target);
        SmartDashboard.putNumber(" Elevator Error ", error);
        SmartDashboard.putNumber(" Elevator Speed ", elevatorSpeed);
        SmartDashboard.putNumber(" Old Elevator Error ", oldError);

        if (error < errorThreshold) {
            SmartDashboard.putString(" Pos ", " True ");
        } else {
            SmartDashboard.putString(" Pos ", " False ");
        }
    }
    
    public void resetElevatorEncoder() {
        elevator.setSelectedSensorPosition(0);
    }
}
