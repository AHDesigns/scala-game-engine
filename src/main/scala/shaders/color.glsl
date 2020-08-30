#version 330 core
layout (location = 0) in vec3 aPos;
out vec4 colour;

uniform vec4 aColor;
uniform float horizontal;
uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;

void main()
{
    vec3 halfSize = aPos * 0.5f;
    gl_Position = projectionMatrix * transformationMatrix * vec4(halfSize.xyz, 1.0);
    colour = aColor;
}

    #---
    #version 330 core

in vec4 colour;
out vec4 FragColor;

void main()
{
    FragColor = colour;
}
