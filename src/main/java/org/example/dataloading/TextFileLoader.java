package org.example.dataloading;

import lombok.Setter;
import org.example.controller.*;
import org.example.requests_responses.trainee.CreateTraineeRequest;
import org.example.requests_responses.trainee.UpdateTraineeProfileRequest;
import org.example.requests_responses.trainer.CreateTrainerRequest;
import org.example.requests_responses.trainer.UpdateTrainerProfileRequest;
import org.example.requests_responses.training.CreateTrainingForTraineeRequest;
import org.example.requests_responses.trainingpartnership.UpdateTrainingPartnershipListRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;

/*
Supported commands:

register-trainee String(firstname) String(lastname) Instant(dateOfBirth) String(address)
register-trainer String(firstname) String(lastname) String(trainingType)
update-trainee String(username) String(new firstname) String(new lastname) Instant(new dateOfBirth) String(new address) boolean(new isActive)
update-trainer String(username) String(new firstname) String(new lastname) boolean(new isActive)
update-partnerships String(trainee) String(trainers)...
add-training String(trainee) String(trainer) String(name) Instant(date) Duration(duration)
add-training-type String(name)
 */

@Component
public class TextFileLoader {
    private static final String DATA_PATH = "/data.txt";

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Setter(onMethod_={@Autowired})
    private CredentialsController credentialsController;

    @Setter(onMethod_={@Autowired})
    private TraineeController traineeController;

    @Setter(onMethod_={@Autowired})
    private TrainerController trainerController;

    @Setter(onMethod_={@Autowired})
    private TrainingController trainingController;

    @Setter(onMethod_={@Autowired})
    private TrainingTypeController trainingTypeController;

    boolean isLoadingRequired() {
        return true;
    }

    @PostConstruct
    void loadData(){
        if (!isLoadingRequired())
            return;

        try (Scanner scanner = new Scanner(Objects.requireNonNull(TextFileLoader.class.getResourceAsStream(DATA_PATH)))) {
            while (scanner.hasNext()) {
                readDataLine(scanner.nextLine());
            }
        } catch (Exception e) {
            log.info("Data loading file not found");
        }
    }

    void readDataLine(String line) {
        try {
            String[] tokens = line.split(" ");
            int argCount = tokens.length;
            if (argCount == 0)
                return;
            switch (tokens[0]) {
                case "register-trainee":
                    if (argCount != 5)
                        throw new Exception("incorrect parameter count. Command format: register-trainee String(firstname) String(lastname) Instant(dateOfBirth) String(address)");
                    credentialsController.createTrainee(new CreateTraineeRequest(tokens[1], tokens[2], tokens[3], tokens[4]));
                    break;
                case "register-trainer":
                    if (argCount != 4)
                        throw new Exception("incorrect parameter count. Command format: register-trainer String(firstname) String(lastname) String(trainingType)");
                    credentialsController.createTrainer(new CreateTrainerRequest(tokens[1], tokens[2], tokens[3]));
                    break;
                case "update-trainee":
                    if (argCount != 7)
                        throw new Exception("incorrect parameter count. Command format: update-trainee String(username) String(new firstname) String(new lastname) Instant(new dateOfBirth) String(new address) boolean(new isActive)");
                    traineeController.updateTrainee(new UpdateTraineeProfileRequest(tokens[2], tokens[3], tokens[4], tokens[5], tokens[6]), tokens[1]);
                    break;
                case "update-trainer":
                    if (argCount != 5)
                        throw new Exception("incorrect parameter count. Command format: update-trainer String(username) String(new firstname) String(new lastname) boolean(new isActive)");
                    trainerController.updateTrainer(new UpdateTrainerProfileRequest(tokens[2], tokens[3], tokens[4]), tokens[1]);
                    break;
                case "update-partnerships":
                    if (argCount == 1)
                        throw new Exception("incorrect parameter count. Command format: update-partnerships String(trainee) String(trainers)...");
                    traineeController.updatePartnerships(new UpdateTrainingPartnershipListRequest(Arrays.copyOfRange(tokens, 2, argCount)), tokens[1]);
                    break;
                case "add-training":
                    if (argCount != 6)
                        throw new Exception("incorrect parameter count. Command format: add-training String(trainee) String(trainer) String(name) Instant(date) Duration(duration)");
                    trainingController.createTraineeTraining(new CreateTrainingForTraineeRequest(tokens[2], tokens[3], tokens[4], tokens[5]), tokens[1]);
                    break;
                case "add-training-type":
                    if (argCount != 2)
                        throw new Exception("incorrect parameter count. Command format: add-training-type String(name)");
                    trainingTypeController.createTrainingType(tokens[1]);
                    break;
                default:
                    if (!tokens[0].startsWith("/") && !tokens[0].equals(""))
                        throw new Exception("unknown command");
            }
        }
        catch (Exception e)
        {
            log.error("Could not read line \"" + line + "\"" + ". Reason: " + e.getMessage());
        }

    }

}
