package loaders

case class TexturedMeshJ(
    _vertices: Array[Float],
    _indices: Array[Int],
    normals: Array[Float],
    textures: Array[Float]
) {
  val vertices: List[Float] = _vertices.toList
  val indices: List[Int] = _indices.toList
}
