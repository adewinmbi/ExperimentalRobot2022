package frc.robot;

import edu.wpi.first.wpilibj2.command.CommandGroupBase;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import edu.wpi.first.wpilibj2.command.button.JoystickButton;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.DemandType;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.sensors.CANCoderStatusFrame;

import edu.wpi.first.wpilibj.PS4Controller;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.PS4Controller.Button;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.EverybotClimber;
import frc.robot.subsystems.climber.ArmTrapezoid;
import frc.robot.subsystems.climber.Elevator;
import frc.robot.Constants.ClimberConstants;
import frc.robot.Constants.DriveConstants;
import frc.robot.Constants.EverybotConstants;

public class RobotContainer {

    public enum Climber {
        LOW,
        TRAVERSAL;
    }

    public Drivetrain drivetrain = new Drivetrain();
    public EverybotClimber everybotClimber = new EverybotClimber();

    public PS4Controller ps4Controller;
    public PS4Controller ps4Controller2;
    
    public SendableChooser<CommandGroupBase> autoChooser;
    public SendableChooser<Climber> climberChooser;

    public Climber climber;

    public ArmTrapezoid armTrapezoid = new ArmTrapezoid();
    public Elevator elevator = new Elevator();

    private boolean m_climberShifter;

    public RobotContainer() {
        SmartDashboard.putBoolean("arm moving", false);
        initSmartDashboard();
        m_climberShifter = true;

        ps4Controller = new PS4Controller(0);
        ps4Controller2 = new PS4Controller(1);
        //drivetrain.compressor.enableDigital();
    }

    // test if this works.

    public void configureButtonBindings() {
        // Assign instantcommands to each PS4 button
        if (climberChooser.getSelected() == Climber.TRAVERSAL) {
            if (ps4Controller2.getL1ButtonPressed()) {
                armTrapezoid.setPositionMotionMagic(ClimberConstants.kTicksToRungAngle);
                SmartDashboard.putString(" Button State ", "L1");
            }
    
            if (ps4Controller2.getL2ButtonPressed()) {
                armTrapezoid.setPositionMotionMagic(ClimberConstants.kTicksToClearRung);
                SmartDashboard.putString(" Button State ", "L2");
            }
    
            if (ps4Controller2.getR1ButtonPressed()) {
                armTrapezoid.setPositionMotionMagic(ClimberConstants.kTicksToVertical);
                SmartDashboard.putString(" Button State ", "R1");
            }
    
            if (ps4Controller2.getR2ButtonPressed()) {
                elevator.elevator.setNeutralMode(NeutralMode.Brake);
                SmartDashboard.putString(" Button State ", "R2 ");
            }
    
            if (ps4Controller2.getSquareButton()) {
                if (elevator.elevator.getSelectedSensorPosition() > ClimberConstants.kElevatorTicksDown ){
                    elevator.elevator.set(ControlMode.PercentOutput, -0.4);
                    SmartDashboard.putString(" Running Command ", "Elevator Down ");
                } else if (elevator.elevator.getSelectedSensorPosition() <= ClimberConstants.kElevatorTicksDown) {
                    elevator.elevator.set(ControlMode.PercentOutput, 0);
                }
                SmartDashboard.putString( "Button State ", "Square ");
            } else if (ps4Controller2.getSquareButton() == false) {
                elevator.elevator.set(ControlMode.PercentOutput, 0);
            }
            
            if (ps4Controller2.getTriangleButton()) {
                if (elevator.elevator.getSelectedSensorPosition() < ClimberConstants.kElevatorTicksUp) {
                    elevator.elevator.set(ControlMode.PercentOutput, 0.32);
                    SmartDashboard.putString(" Running Command ", "Elevator Up ");
                } else if (elevator.elevator.getSelectedSensorPosition() > ClimberConstants.kElevatorTicksUp) {
                    elevator.elevator.set(ControlMode.PercentOutput, 0);
                } 
                SmartDashboard.putString( "Button State ", " Triangle ");
            } else if (ps4Controller2.getTriangleButton() == false) {
                    elevator.elevator.set(ControlMode.PercentOutput, 0);
            }

            if (ps4Controller2.getCircleButton()) {
                if (elevator.elevator.getSelectedSensorPosition() < ClimberConstants.kElevatorTicksExtend) {
                    elevator.elevator.set(ControlMode.PercentOutput, 0.32);
                    SmartDashboard.putString(" Running Command ", "Elevator Up Extend ");
                } else if (elevator.elevator.getSelectedSensorPosition() > ClimberConstants.kElevatorTicksExtend) {
                    elevator.elevator.set(ControlMode.PercentOutput, 0);
                }
                SmartDashboard.putString(" Button State ", " Circle ");
            } else if (ps4Controller2.getCircleButton() ==  false) {
                elevator.elevator.set(ControlMode.PercentOutput, 0);
            }
    
            double armInput = -ps4Controller2.getRightY();
            armTrapezoid.arm.set(ControlMode.PercentOutput, armInput * 0.25, DemandType.ArbitraryFeedForward, -1 * armTrapezoid.FF());

            // Gear shifting
            // Actually triangle button
            if (ps4Controller.getTriangleButtonPressed()) {
                // Shifts to high gear
                drivetrain.driveShifter.set(Value.kForward);
                SmartDashboard.putString(" Button State ", "A");
                // highGear = true;
            }

            // Actually square button
            if (ps4Controller.getCircleButtonPressed()) {
                // Shifts to low gear
                drivetrain.driveShifter.set(Value.kReverse);
                SmartDashboard.putString(" Button State ", "B");
                // highGear = false;
            }

            if (ps4Controller2.getCrossButtonPressed()) {
                if (m_climberShifter == true) {
                    drivetrain.climberShifter.set(Value.kReverse);
                    m_climberShifter = false;
                } else if (m_climberShifter == false) {
                    drivetrain.climberShifter.set(Value.kForward);
                    m_climberShifter = true;
                }
                SmartDashboard.putString(" Button State ", "Cr");
                SmartDashboard.putBoolean(" Climber Piston Forward ", m_climberShifter);
            }

                if (ps4Controller2.getLeftY() > ClimberConstants.kOperatorDeadband) {
                drivetrain.hookShifter.set(Value.kForward);
                } else if (ps4Controller2.getLeftY() < ClimberConstants.kOperatorDeadband) {
                drivetrain.hookShifter.set(Value.kReverse);
                }

        } 
        
        else if (climberChooser.getSelected() == Climber.LOW) {
            // Actually Cross
            if (ps4Controller2.getSquareButtonPressed()) {
                everybotClimber.moveClimber(1 * EverybotConstants.kTicksToLowRung);
                SmartDashboard.putBoolean("Moving to low rung", true);
                SmartDashboard.putString(" Button State ", " PS1 Square ");
            }
            // Actually Circle
            if (ps4Controller2.getCrossButtonPressed()) {
                everybotClimber.moveClimber(1 * EverybotConstants.kTicksToClimbLowRung);
                SmartDashboard.putBoolean("Climbing onto low rung", true);
                SmartDashboard.putString(" Button State ", " PS1 Cross ");
            }
        }

        // TELEOP DRIVE
        double leftInput = ps4Controller.getLeftY();
        double rightInput = ps4Controller.getRightY();
        double prevLeftOutput = drivetrain.leftMaster.getMotorOutputPercent();
        double prevRightOutput = drivetrain.rightMaster.getMotorOutputPercent();

        // Low pass filter, output = (alpha * intended value) + (1-alpha) * previous value
        double leftOutput = (DriveConstants.kDriveAlpha * leftInput) + (DriveConstants.kDriveOneMinusAlpha * prevLeftOutput);
        double rightOutput = (DriveConstants.kDriveAlpha * rightInput) + (DriveConstants.kDriveOneMinusAlpha * prevRightOutput);

        drivetrain.rightMaster.set(ControlMode.PercentOutput, rightOutput);
        drivetrain.leftMaster.set(ControlMode.PercentOutput, leftOutput);
        
    }

    
    public void initSmartDashboard() {
        autoChooser = new SendableChooser<CommandGroupBase>();

        autoChooser.setDefaultOption("leave tarmac :)", 
            new SequentialCommandGroup(
                // drive for 1 second with power 0.5, then set power zero
                new ParallelDeadlineGroup(
                    new WaitCommand(1), 
                    new InstantCommand(() -> drivetrain.setPower(0.5, 0.5))
                ), 
                new InstantCommand(() -> drivetrain.setPowerZero())
            )
        );
        
        autoChooser.addOption("delay 5s then taxi",
            new SequentialCommandGroup(
                new WaitCommand(5),
                new ParallelDeadlineGroup(
                    new WaitCommand(1), 
                    new InstantCommand(() -> drivetrain.setPower(0.5, 0.5))
                ),
                new InstantCommand(() -> drivetrain.setPowerZero())
            )
            
        );

        SmartDashboard.putData(autoChooser);

        climberChooser = new SendableChooser<Climber>();

        climberChooser.addOption("Select Low climb", Climber.LOW);
        climberChooser.setDefaultOption("Select Traversal climb", Climber.TRAVERSAL);
        
        SmartDashboard.putData(climberChooser);

        SmartDashboard.putData(" Reset Climber Encoders ", new InstantCommand(() -> everybotClimber.climberMaster.setSelectedSensorPosition(0)));
        
        SmartDashboard.putData(" Move ArmTrapezoid Angle ", new InstantCommand(() -> 
            armTrapezoid.setPositionMotionMagic(ClimberConstants.kTicksToRungAngle)));

        SmartDashboard.putData( " Move ArmTrapezoid Vertical ", new InstantCommand(() ->
            armTrapezoid.setPositionMotionMagic(ClimberConstants.kTicksToVertical)));

        SmartDashboard.putData( "Move ArmTrapezoid Clear Rung ", new InstantCommand(() ->
            armTrapezoid.setPositionMotionMagic(ClimberConstants.kTicksToClearRung)));

        SmartDashboard.putData( "Reset Arm Encoder ", new InstantCommand(() -> 
            armTrapezoid.resetClimbEncoder()));
        
        SmartDashboard.putData(" Reset Elevator Encoder ", new InstantCommand(() ->
            elevator.resetElevatorEncoder()));

        SmartDashboard.putData(" Command Scheduler Disable ", new InstantCommand(() -> 
            CommandScheduler.getInstance().disable()));

        SmartDashboard.putData(" Elevator Coast Mode ", new InstantCommand(() ->
            elevator.elevator.setNeutralMode(NeutralMode.Coast)));
        
        SmartDashboard.putData(" Elevator Brake Mode ", new InstantCommand(() ->
            elevator.elevator.setNeutralMode(NeutralMode.Brake)));
            
    }

    public void reportToSmartDashboard() {

        SmartDashboard.putNumber(" Climber Position", everybotClimber.climberMaster.getSelectedSensorPosition());
        SmartDashboard.putNumber(" Arm Position ", armTrapezoid.arm.getSelectedSensorPosition());
        SmartDashboard.putNumber(" Arm Velocity ", armTrapezoid.arm.getSelectedSensorVelocity());
        SmartDashboard.putNumber(" Arm Voltage ", armTrapezoid.arm.getMotorOutputVoltage());
        SmartDashboard.putNumber(" Arm Angle Conversion ", armTrapezoid.ticksToAngle());
        SmartDashboard.putNumber(" Elevator Position ", elevator.elevator.getSelectedSensorPosition());
        SmartDashboard.putNumber(" Elevator Voltage ", elevator.elevator.getMotorOutputVoltage());
        // SmartDashboard.putBoolean(" Triangle Button Held ", ps4Controller2.getTriangleButton());
        // SmartDashboard.putNumber(" Right Operator Axis ", ps4Controller2.getRightY());
        // SmartDashboard.putNumber(" ArmMM Position ", armMotionMagic.arm.getSelectedSensorPosition());
        // SmartDashboard.putNumber(" ArmMM Velocity", armMotionMagic.arm.getSelectedSensorVelocity());
        // SmartDashboard.putNumber(" ArmMM Voltage ", armMotionMagic.arm.getMotorOutputVoltage());
        SmartDashboard.putBoolean(" Triangle Button Held ", ps4Controller2.getTriangleButton());
        SmartDashboard.putNumber(" Climber Voltage ", everybotClimber.climberMaster.getMotorOutputVoltage());
        SmartDashboard.putNumber(" Climber Current ", everybotClimber.climberMaster.getSupplyCurrent());
        SmartDashboard.putNumber(" Left Operator Y Axis ", ps4Controller2.getLeftY());

    }
}
