using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SpaceVIL.Core
{
    /// <summary>
    /// An interface that describes update events by type of event.
    /// <para/> This interface is part of SpaceVIL.Core.IBaseItem.
    /// </summary>
    public interface IEventUpdate
    {
        /// <summary>
        /// Method for updating an item size or/and position.
        /// </summary>
        /// <param name="type">Type of event as SpaceVIL.Core.GeometryEventType.</param>
        /// <param name="value">Value of a property that was changed.</param>
        void Update(GeometryEventType type, int value);
    }
}
