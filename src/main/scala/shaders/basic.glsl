#version 330 core
layout (location = 0) in vec3 aPos;
layout (location = 1) in vec3 normal;

out vec4 colour;
out vec3 surfaceNormal;
out vec3 toLightVector;

uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform vec3 lightPos;

uniform vec4 aColor;

void main()
{
    vec4 worldPos = transformationMatrix * vec4(aPos, 1.0);
    gl_Position = projectionMatrix * viewMatrix * worldPos;
    colour = aColor;

    surfaceNormal = (transformationMatrix * vec4(normal, 0)).xyz;
    toLightVector = lightPos - worldPos.xyz;
}

    #---
    #version 330 core

in vec4 colour;
in vec3 surfaceNormal;
in vec3 toLightVector;

out vec4 FragColor;

uniform vec3 lightCol;

void main()
{
    vec3 unitNormal = normalize(surfaceNormal);
    vec3 unitLightVector = normalize(toLightVector);
    float nDot1 = dot(unitNormal, unitLightVector);
    float brightness = max(nDot1, 0.3);
    vec3 diffuse = brightness * lightCol;

    FragColor = vec4(diffuse, 1.0) * colour;
}
