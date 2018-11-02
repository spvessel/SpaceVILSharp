using System;

[Serializable]
public class SpaceVILException : Exception
{
    public SpaceVILException() { }
    public SpaceVILException(string message) : base(message) { }
}