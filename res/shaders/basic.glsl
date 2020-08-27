#version 330 core
layout (location = 0) in vec3 aPos;
layout (location = 1) in vec3 aColor;

out vec3 colour;

void main()
{
    gl_Position = vec4(aPos.xyz, 1.0);
    colour = aColor;
}

    #---
    #version 330 core

in vec3 colour;
out vec4 FragColor;

void main()
{
    FragColor = vec4(colour, 1.0f);
}
