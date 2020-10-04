#version 330 core
layout (location = 0) in vec3 aPos;
layout (location = 1) in vec3 normal;
layout (location = 3) in vec2 texCoord;

out vec2 textureCoord;
out vec3 surfaceNormal;
out vec3 toLightVector;

uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform vec3 lightPos;

void main()
{
    vec4 worldPos = transformationMatrix * vec4(aPos, 1.0);
    gl_Position = projectionMatrix * viewMatrix * worldPos;
    textureCoord = texCoord;

    surfaceNormal = (transformationMatrix * vec4(normal, 0)).xyz;
    toLightVector = lightPos - worldPos.xyz;
}

    #---
    #version 330 core

in vec2 textureCoord;
in vec3 surfaceNormal;
in vec3 toLightVector;

out vec4 FragColor;

uniform vec3 lightCol;
uniform sampler2D textureSampler;

void main()
{
    vec3 unitNormal = normalize(surfaceNormal);
    vec3 unitLightVector = normalize(toLightVector);
    float nDot1 = dot(unitNormal, unitLightVector);
    float brightness = max(nDot1, 0.3);
    vec3 diffuse = brightness * lightCol;
    vec4 white = vec4(1,1,1,1);

//    FragColor = vec4(diffuse, 1.0) * white;
    vec4 colour = texture(textureSampler, textureCoord);
//    colour.y = textureCoord.y;
//    colour.x = textureCoord.x
//    colour.w = 1;
    FragColor = colour;
}
