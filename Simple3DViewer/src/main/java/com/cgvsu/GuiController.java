package com.cgvsu;

import com.cgvsu.math.Matrix4f;
import com.cgvsu.math.Vector2f;
import com.cgvsu.math.Vector3f;
import com.cgvsu.math.Vector4f;
import com.cgvsu.model.Model;
import com.cgvsu.model.Polygon;
import com.cgvsu.objreader.ObjReader;
import com.cgvsu.objwriter.ObjWriter;
import com.cgvsu.render_engine.Camera;
import com.cgvsu.render_engine.GraphicConveyor;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Optional;

import static com.cgvsu.render_engine.RenderEngine.mainRender;

public class GuiController {

    final private float TRANSLATION = 0.5F;
    public SplitPane splitPane;
    public MenuItem menuItemSaveModel;

    @FXML
    private Canvas canvas;
    @FXML
    private TextField textFieldScaleX;
    @FXML
    private TextField textFieldScaleY;
    @FXML
    private TextField textFieldScaleZ;
    @FXML
    private TextField textFieldRotationX;
    @FXML
    private TextField textFieldRotationY;
    @FXML
    private TextField textFieldRotationZ;
    @FXML
    private TextField textFieldTranslationX;
    @FXML
    private TextField textFieldTranslationY;
    @FXML
    private TextField textFieldTranslationZ;

    private Model mesh = null;

    private Camera camera = new Camera(
            new Vector3f(0, 0, 100),
            new Vector3f(0, 0, 0),
            1.0F, 1, 0.01F, 100);

    private Timeline timeline;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private AnchorPane anchorPaneForCanvas;

    private static ArrayList<Vector3f> getVector3fs(TransformationParameters params) {
        Vector3f rotation = new Vector3f((float) params.getAlpha(), (float) params.getBeta(), (float) params.getGamma());
        Vector3f scale = new Vector3f((float) params.getScaleX(), (float) params.getScaleY(), (float) params.getScaleZ());
        Vector3f translation = new Vector3f(-(float) params.getTranslationX(), (float) params.getTranslationY(), (float) params.getTranslationZ());

        ArrayList<Vector3f> trans = new ArrayList<>();
        trans.add(rotation);
        trans.add(scale);
        trans.add(translation);
        return trans;
    }

    @FXML
    private void initialize() {
        anchorPane.prefWidthProperty().addListener((ov, oldValue, newValue) -> canvas.setWidth(newValue.doubleValue()));
        anchorPane.prefHeightProperty().addListener((ov, oldValue, newValue) -> canvas.setHeight(newValue.doubleValue()));

        timeline = new Timeline();
        timeline.setCycleCount(Animation.INDEFINITE);

        KeyFrame frame = new KeyFrame(Duration.millis(15), event -> {
            double width = canvas.getWidth();
            double height = canvas.getHeight();

            canvas.getGraphicsContext2D().clearRect(0, 0, width, height);
            camera.setAspectRatio((float) (width / height));

            if (mesh != null) {
                mainRender(canvas.getGraphicsContext2D(), camera, mesh, width, height);
            }
        });

        timeline.getKeyFrames().add(frame);
        timeline.play();
    }

    private FileChooser createFileChooser(String description, String extension, String title) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(description, extension));
        fileChooser.setTitle(title);
        File initialDirectory = new File("Simple3DViewer/src/main/resources/3DModels");
        if (initialDirectory.exists()) {
            fileChooser.setInitialDirectory(initialDirectory);
        }
        return fileChooser;
    }


    @FXML
    private void onOpenModelMenuItemClick() {
        FileChooser fileChooser = createFileChooser("Model (*.obj)", "*.obj", "Load Model");
        fileChooser.setTitle("Load Model");

        File file = fileChooser.showOpenDialog((Stage) canvas.getScene().getWindow());
        if (file == null) {
            return;
        }

        Path fileName = Path.of(file.getAbsolutePath());

        try {
            String fileContent = Files.readString(fileName);
            mesh = ObjReader.read(fileContent);
            // todo: обработка ошибок
        } catch (IOException exception) {

        }
    }

    private void moveCamera(Vector3f delta) {
        camera.setPosition(camera.getPosition().add(delta));
        render();
    }

    private void render() {
        double width = canvas.getWidth();
        double height = canvas.getHeight();


        canvas.getGraphicsContext2D().clearRect(0, 0, width, height);
        camera.setAspectRatio((float) (width / height));

        mainRender(canvas.getGraphicsContext2D(), camera, mesh, width, height);
    }

    @FXML
    private void handleCameraForward(ActionEvent actionEvent) {
        moveCamera(new Vector3f(0, 0, -TRANSLATION));
    }

    @FXML
    private void handleCameraBackward(ActionEvent actionEvent) {
        moveCamera(new Vector3f(0, 0, TRANSLATION));
    }

    @FXML
    public void onClickCanvas() {
        canvas.requestFocus();
    }

    @FXML
    private void handleCameraLeft(ActionEvent actionEvent) {
        moveCamera(new Vector3f(TRANSLATION, 0, 0));
    }

    @FXML
    private void handleCameraRight(ActionEvent actionEvent) {
        moveCamera(new Vector3f(-TRANSLATION, 0, 0));
    }

    @FXML
    private void handleCameraUp(ActionEvent actionEvent) {
        moveCamera(new Vector3f(0, TRANSLATION, 0));
    }

    @FXML
    private void handleCameraDown(ActionEvent actionEvent) {
        moveCamera(new Vector3f(0, -TRANSLATION, 0));
    }

    private TransformationParameters parseParameters() throws NumberFormatException {
        TransformationParameters params = new TransformationParameters();
        params.setAlpha(Double.parseDouble(textFieldRotationX.getText()));
        params.setBeta(Double.parseDouble(textFieldRotationY.getText()));
        params.setGamma(Double.parseDouble(textFieldRotationZ.getText()));
        params.setTranslationX(Double.parseDouble(textFieldTranslationX.getText()));
        params.setTranslationY(Double.parseDouble(textFieldTranslationY.getText()));
        params.setTranslationZ(Double.parseDouble(textFieldTranslationZ.getText()));
        params.setScaleX(Double.parseDouble(textFieldScaleX.getText()));
        params.setScaleY(Double.parseDouble(textFieldScaleY.getText()));
        params.setScaleZ(Double.parseDouble(textFieldScaleZ.getText()));
        return params;
    }


    @FXML
    public void recalculationOfNormals(ActionEvent actionEvent) {
        TransformationParameters params;
        try {
            params = parseParameters();
            if (!getVector3fs(params).get(1).positiveVector()) {

                return;
            }
        } catch (NumberFormatException e) {

            return;
        }
        recalculationOfNormals(getVector3fs(params));
    }

    private void recalculationOfNormals(ArrayList<Vector3f> list) {
        Matrix4f transformationMatrix = GraphicConveyor.translateRotateScale(list.get(2), list.get(0), list.get(1));

        ArrayList<Vector3f> transformedVertices = new ArrayList<>();
        for (Vector3f vertex : mesh.getOriginalVertices()) {
            Vector3f transformedVertex = transformationMatrix.multiply3(new Vector4f(vertex));
            transformedVertices.add(transformedVertex);
        }

        mesh.setVertices(transformedVertices);
        mesh.setNormals(NormalCalculator.calculateNormals(mesh));

        render();
    }

    public void saveModelFromFile() {
        if (mesh == null) {

            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Save Model");
        alert.setHeaderText("Choose save option:");
        alert.setContentText("Do you want to save the modified version or the original version?");

        ButtonType buttonModified = new ButtonType("Save Modified");
        ButtonType buttonOriginal = new ButtonType("Save Original");
        ButtonType buttonCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonModified, buttonOriginal, buttonCancel);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == buttonModified) {
            saveModels(false);
        } else if (result.isPresent() && result.get() == buttonOriginal) {
            saveModels(true);
        }
    }

    private void saveModels(boolean useOriginalModel) {
        ObjWriter objWriter = new ObjWriter();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Model");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("OBJ Files", "*.obj"));
        File initialDirectory = new File("Saved models");
        if (initialDirectory.exists()) {
            fileChooser.setInitialDirectory(initialDirectory);
        }
            fileChooser.setInitialFileName("Model" + ".obj");
            File file = fileChooser.showSaveDialog(null);

            if (file != null) {
                ArrayList<Vector3f> vertices = useOriginalModel ? mesh.getOriginalVertices() : mesh.getVertices();
                ArrayList<Polygon> polygons = mesh.getPolygons();
                ArrayList<Vector2f> textureVertices =  mesh.getTextureVertices();
                ArrayList<Vector3f> normals = mesh.getNormals();
                objWriter.writeModelToObjFile(
                        file.getAbsolutePath(),
                        vertices,
                        textureVertices,
                        normals,
                        polygons
                );
            }
    }
}