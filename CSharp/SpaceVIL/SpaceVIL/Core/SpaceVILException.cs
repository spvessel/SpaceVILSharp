using System;

namespace SpaceVIL.Core
{
    /// <summary>
    /// SpaceVILException is a class of exceptions of the SpaceVIL.
    /// </summary>
    [Serializable]
    public class SpaceVILException : Exception
    {
        public SpaceVILException() { }
        public SpaceVILException(string message) : base(message) { }
    }
}