package loaders

import scala.xml._

/**
 * Dae format from blender notes
 * library_geometries contains vertex data
 * triangles \ p lists info about indices
 * triangles \ @material links to library_materials / @id, which in turn links to a library effect
 */

/**
 * Load dae model format
 */
trait DaeLoader {
  def load(file: String) = {
    val xml = XML.load(s"res/models/$file.dae")

    val data = xml \ "library_geometries"
    //    val materials = xml \ "library_materials"
    //    val effects = xml \ "library_effects"
    //    val material = data \ "geometry" \ "mesh" \ "triangles" \ "@material"
    val triangles = data \ "geometry" \ "mesh" \ "triangles"
    //    println(material)
    val from = getSrc(triangles \ "input")
    val positions = get(triangles \ "p")
    val vertices = from("VERTEX")
    val normals = from("NORMAL")
    println(vertices)
    println(normals)
    println(positions)
  }

  private def getSrc(inputs: NodeSeq): String => Either[String, String] = (target: String) => {
    inputs.find(where("semantic", target)).flatMap(_.attribute("source")) match {
      case Some(value) => Right(value.text)
      case None => Left(s"failed to find source for $target")
    }
  }

  private def where(attribute: String, is: String)(node: Node): Boolean = node.attribute(attribute).getOrElse("").toString == is

  private def get(data: NodeSeq): Option[String] = data map (_.text) match {
    case Nil => None
    case head :: _ => Some(head)
  }

  class Input(val source: String, _offset: String) {
    val offset: Int = try {
      _offset.toInt
    } catch {
      case _: Throwable =>
        println(s"Malformed dae file! input / offset value '${_offset}'could not be parsed to Int")
        System.exit(1)
        0
    }
  }

}
