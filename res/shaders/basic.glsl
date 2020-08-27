#version 330 core
layout (location = 0) in vec3 aPos;

out vec4 colour;

void main()
{
    gl_Position = vec4(aPos.xy * 0.5, aPos.z, 1.0);
    colour = vec4(aPos.xyz, 1.0);
}

    #---
    #version 330 core

out vec4 FragColor;

void main()
{
    FragColor = vec4(1.0f, 0.5f, 0.44f, 1.0f);
}
