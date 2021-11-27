package org.team1619.behavior;

import org.uacr.models.behavior.Behavior;
import org.uacr.shared.abstractions.InputValues;
import org.uacr.shared.abstractions.OutputValues;
import org.uacr.shared.abstractions.RobotConfiguration;
import org.uacr.utilities.Config;
import org.uacr.utilities.Timer;
import org.uacr.utilities.logging.LogManager;
import org.uacr.utilities.logging.Logger;

import java.util.Set;

/**
 * Example behavior to copy for other behaviors
 */

public class Drivetrain_Percent implements Behavior {

	private static final Logger sLogger = LogManager.getLogger(Behavior_Example.class);
	private static final Set<String> sSubsystems = Set.of("ss_drivetrain");

	private final InputValues fSharedInputValues;
	private final OutputValues fSharedOutputValues;

	private double xAxis;
	private double yAxis;


	public Drivetrain_Percent(InputValues inputValues, OutputValues outputValues, Config config, RobotConfiguration robotConfiguration) {
		fSharedInputValues = inputValues;
		fSharedOutputValues = outputValues;
	}

	@Override
	public void initialize(String stateName, Config config) {
		sLogger.debug("Entering state {}", stateName);

	}

	@Override
	public void update() {
		xAxis = fSharedInputValues.getNumeric("ipn_driver_right_x");
		yAxis = fSharedInputValues.getNumeric("ipn_driver_left_y");

		double leftMotorSpeed = xAxis + yAxis;
		double rightMotorSpeed = yAxis - xAxis;

		if (leftMotorSpeed > 1) {
			rightMotorSpeed = rightMotorSpeed - (leftMotorSpeed - 1);
			leftMotorSpeed = 1;
		}
		else if (leftMotorSpeed < -1) {
			rightMotorSpeed = rightMotorSpeed - (leftMotorSpeed + 1);
			leftMotorSpeed = -1;
		}
		else if (rightMotorSpeed > 1) {
			leftMotorSpeed = leftMotorSpeed - (rightMotorSpeed - 1);
			rightMotorSpeed = 1;
		}
		else if (rightMotorSpeed < -1) {
			leftMotorSpeed = leftMotorSpeed - (rightMotorSpeed + 1);
			rightMotorSpeed = -1;
		}

		fSharedOutputValues.setNumeric("opn_drivetrain_left", "percent", leftMotorSpeed); //hi
		fSharedOutputValues.setNumeric("opn_drivetrain_right", "percent", rightMotorSpeed);
	}

	@Override
	public void dispose() {
		fSharedOutputValues.setNumeric("opn_drivetrain_left", "percent", 0.0);
		fSharedOutputValues.setNumeric("opn_drivetrain_right", "percent", 0.0);
	}

	@Override
	public boolean isDone() {
		return false;
	}

	@Override
	public Set<String> getSubsystems() {
		return sSubsystems;
	}
}