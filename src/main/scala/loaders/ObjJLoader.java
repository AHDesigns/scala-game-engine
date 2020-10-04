package loaders;

import org.joml.Vector2f;
import org.joml.Vector3f;
import rendy.TexturedMeshJ;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class ObjJLoader {
    public static TexturedMeshJ load(String fileName) {
        FileReader fr = null;
        try {
            fr = new FileReader(new File("res/models/" + fileName + ".obj"));
        } catch (FileNotFoundException e) {
            System.err.print("Couldn't load file! " + fileName + "\n");
            e.printStackTrace();
        }

        BufferedReader reader = new BufferedReader(fr);
        String line;
        List<Vector3f> vertices = new ArrayList<>();
        List<Vector2f> textures = new ArrayList<>();
        List<Vector3f> normals = new ArrayList<>();
        List<Integer> indicies = new ArrayList<>();
        float[] verticiesArray = null;
        float[] normalsArray = null;
        float[] texturesArray = null;
        int[] indiciesArray = null;
        try {
            while (true) {
                line = reader.readLine();
                String[] currentLine = line.split(" ");
                if (line.startsWith("v ")) {
                    vertices.add(new Vector3f(
                            Float.parseFloat(currentLine[1]),
                            Float.parseFloat(currentLine[2]),
                            Float.parseFloat(currentLine[3])
                    ));
                } else if (line.startsWith("vt ")) {
                    textures.add(new Vector2f(
                            Float.parseFloat(currentLine[1]),
                            Float.parseFloat(currentLine[2])
                    ));
                } else if (line.startsWith("vn ")) {
                    normals.add(new Vector3f(
                            Float.parseFloat(currentLine[1]),
                            Float.parseFloat(currentLine[2]),
                            Float.parseFloat(currentLine[3])
                    ));
                } else if (line.startsWith("f ")) {
                    texturesArray = new float[vertices.size() * 2];
                    normalsArray = new float[vertices.size() * 3];
                    break;
                }
            }

            while (line != null) {
                if (!line.startsWith("f ")) {
                    line = reader.readLine();
                    continue;
                }

                String[] currentLine = line.split(" ");
                String[] vertex1 = currentLine[1].split("/");
                String[] vertex2 = currentLine[2].split("/");
                String[] vertex3 = currentLine[3].split("/");

                processVertex(vertex1, indicies, textures, normals, texturesArray, normalsArray);
                processVertex(vertex2, indicies, textures, normals, texturesArray, normalsArray);
                processVertex(vertex3, indicies, textures, normals, texturesArray, normalsArray);
                line = reader.readLine();
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        verticiesArray = new float[vertices.size() * 3];
        indiciesArray = new int[indicies.size()];
        int vertexPointer = 0;
        for (Vector3f vertex :
                vertices) {
            verticiesArray[vertexPointer++] = vertex.x;
            verticiesArray[vertexPointer++] = vertex.y;
            verticiesArray[vertexPointer++] = vertex.z;
        }

        for (int i = 0; i < indicies.size(); i++) {
            indiciesArray[i] = indicies.get(i);
        }
        return new TexturedMeshJ(
                verticiesArray,
                indiciesArray,
                normalsArray,
                texturesArray
        );
    }

    private static void processVertex(
            String[] vertexData,
            List<Integer> indices,
            List<Vector2f> textures,
            List<Vector3f> normals,
            float[] textArray,
            float[] normalsArray
    ) {
        int currentVertexPointer = Integer.parseInt(vertexData[0]) - 1;
        indices.add(currentVertexPointer);

        Vector2f currentTex = textures.get(Integer.parseInt(vertexData[1]) - 1);
        textArray[currentVertexPointer * 2] = currentTex.x;
        textArray[currentVertexPointer * 2 + 1] = 1 - currentTex.y;

        Vector3f currentNorm = normals.get(Integer.parseInt(vertexData[2]) - 1);
        normalsArray[currentVertexPointer * 3] = currentNorm.x;
        normalsArray[currentVertexPointer * 3 + 1] = currentNorm.y;
        normalsArray[currentVertexPointer * 3 + 2] = currentNorm.z;
    }

}
