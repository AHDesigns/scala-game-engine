package loaders

import org.joml.{Vector2f, Vector3f}
import rendy._

import scala.collection.mutable

/**
  * Notes on exporting OBJ from blender
  * https://www.youtube.com/watch?v=KMWUjNE0fYI
  * include normals
  * include uvs
  * triangulate faces
  * objects as OBJ objects
  * "forward" -Z Forward
  * Up Y Up
  */

/**
  * Load OBJ from res/models
  */
trait ObjLoader extends FileLoader {
  protected def load(file: String): ModelData = {
    ObjJLoader.load(file) match {
      case TexturedModelJ(vertices, indices, normals, textures) =>
        ModelData(
          PositionsData(vertices.toList),
          indices.toList,
          normalsData = Some(NormalsData(normals.toList)),
          textures = Some(TextureData(textures.toList))
        )
    }
  }

  // TODO: fix this and deprecate the method above
  private def loadScala(file: String) = {
    val vertices = mutable.ArrayBuffer.empty[Vector3f]
    val textures = mutable.ArrayBuffer.empty[Vector2f]
    val normals = mutable.ArrayBuffer.empty[Vector3f]
    val indices = mutable.ArrayBuffer.empty[Int]

    val verticesArray = mutable.ArrayBuffer.empty[Float]
    val texturesArray = mutable.ArrayBuffer.empty[Float]
    val normalsArray = mutable.ArrayBuffer.empty[Float]
    val indicesArray = mutable.ArrayBuffer.empty[Int]

    readFileByLines(s"res/models/$file.obj") { source =>
      {
        source.getLines().foreach { line =>
          println(s"processing line $line")
          line.split(" ") match {
            case Array("v", x, y, z) =>
              vertices += new Vector3f(F(x), F(y), F(z))
            case Array("vt", u, v) => textures += new Vector2f(F(u), F(v))
            case Array("vn", x, y, z) =>
              normals += new Vector3f(F(x), F(y), F(z))
            case Array("f", v1, v2, v3) =>
              processVertex(
                v1,
                indices,
                textures,
                normals,
                texturesArray,
                normalsArray
              );
              processVertex(
                v2,
                indices,
                textures,
                normals,
                texturesArray,
                normalsArray
              );
              processVertex(
                v3,
                indices,
                textures,
                normals,
                texturesArray,
                normalsArray
              );
            case _ => ;
          }
        }
        var vertexPointer = 0;
        for (vertex <- vertices) {
          verticesArray(vertexPointer) = vertex.x
          vertexPointer += 1
          verticesArray(vertexPointer) = vertex.y
          vertexPointer += 1
          verticesArray(vertexPointer) = vertex.z
        }
        println("about to loop")
        for (i <- indices.indices) {
          indicesArray(i) = indices(i)
        }
        println("looped")

        //        ComplexModel(
        //          verticesArray.toList,
        //          indicesArray.toList,
        //          List(
        //            NormalsData(normalsArray.toList),
        //            TextureData(texturesArray.toList),
        //          )
        //        )
      }
    }
  }

  private def F(value: String): Float = value.toFloat

  private def processVertex(
      vertex: String,
      indices: mutable.ArrayBuffer[Int],
      textures: mutable.ArrayBuffer[Vector2f],
      normals: mutable.ArrayBuffer[Vector3f],
      textArray: mutable.ArrayBuffer[Float],
      normalsArray: mutable.ArrayBuffer[Float]
  ) {
    val vertexData = vertex.split("/")
    val currentVertexPointer = vertexData(0).toInt - 1
    indices += (currentVertexPointer)
    val currentTex = textures(vertexData(1).toInt - 1)
    textArray(currentVertexPointer * 2) = currentTex.x
    textArray(currentVertexPointer * 2 + 1) = 1 - currentTex.y
    val currentNorm = normals(vertexData(2).toInt - 1)
    normalsArray(currentVertexPointer * 3) = currentNorm.x
    normalsArray(currentVertexPointer * 3 + 1) = currentNorm.y
    normalsArray(currentVertexPointer * 3 + 2) = currentNorm.z
  }
}
