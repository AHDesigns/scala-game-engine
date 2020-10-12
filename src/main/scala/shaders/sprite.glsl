#version 330 core
layout (location = 0) in vec3 aPos;
layout (location = 3) in vec2 texCoord;

out vec2 textureCoord;

uniform mat4 spriteMatrix;

void main()
{
    gl_Position = spriteMatrix * vec4(aPos, 1.0);
    textureCoord = texCoord;
}

    #---
    #version 330 core

in vec2 textureCoord;

out vec4 FragColor;

uniform sampler2D textureSampler;

void main()
{
    FragColor = texture(textureSampler, textureCoord);
}
