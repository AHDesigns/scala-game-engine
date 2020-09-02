#version 330 core
layout (location = 0) in vec3 aPos;

out vec4 colour;

uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform vec3 lightPos;

uniform vec4 aColor;

void main()
{
    gl_Position = projectionMatrix * viewMatrix * transformationMatrix * vec4(aPos.xyz, 1.0);
    colour = aColor;
}

    #---
    #version 330 core

in vec4 colour;
out vec4 FragColor;
uniform vec3 lightCol;

void main()
{
    FragColor = colour;
}
