#version 330 core
layout (location = 0) in vec3 aPos;
layout (location = 2) in vec3 aColor;
out vec4 colour;

//uniform vec4 aColor;
uniform float horizontal;
uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform vec3 lightPos;

void main()
{
    vec3 halfSize = aPos * 0.5f;
    gl_Position = projectionMatrix * viewMatrix * transformationMatrix * vec4(halfSize.xyz, 1.0);
    colour = vec4(aColor, 1.0f);
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
