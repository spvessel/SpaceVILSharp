using System;

namespace SpaceVIL.Core
{
    [Serializable]
    public class SpaceVILException : Exception
    {
        public SpaceVILException() { }
        public SpaceVILException(string message) : base(message) { }
    }
}