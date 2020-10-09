using System;
using System.Collections;
using System.Collections.Generic;
namespace SpaceVIL
{
    internal class LinkedDictionary<TKey, TValue> : IDictionary<TKey, TValue>
    {
        private readonly Dictionary<TKey, LinkedListNode<Tuple<TValue, TKey>>> dict;
        private readonly LinkedList<Tuple<TValue, TKey>> list;

        public LinkedDictionary()
        {
            this.dict = new Dictionary<TKey, LinkedListNode<Tuple<TValue, TKey>>>();
            this.list = new LinkedList<Tuple<TValue, TKey>>();
        }

        public TValue this[TKey key]
        {
            get
            {                
                return dict[key].Value.Item1;
            }
            set 
            {
                if(ContainsKey(key))
                {
                    list.Remove(dict[key]);
                }

                dict[key] = new LinkedListNode<Tuple<TValue, TKey>>(Tuple.Create(value, key));
                list.AddLast(dict[key]);
            }
        }

        public TValue PopFirst()
        {
            var node = list.First;
            list.Remove(node);
            dict.Remove(node.Value.Item2);
            return node.Value.Item1;
        }

        public TValue PeekFirst()
        {
            return list.First.Value.Item1;
        }

        public ICollection<TKey> Keys
        {
            get
            {
                return dict.Keys;
            }
        }

        public ICollection<TValue> Values
        {
            get
            {
                List<TValue> vals = new List<TValue>();
                
                for (LinkedListNode<Tuple<TValue, TKey>> node = list.First; node != null; node = node.Next)
                {
                    vals.Add(node.Value.Item1);
                }

                return vals;
            }
        }

        public int Count 
        {
            get
            {
                return dict.Count;
            }
        }

        public bool IsReadOnly
        {
            get
            {
                throw new NotImplementedException();
            }
        }

        public void Add(TKey key, TValue value)
        {
            if (key == null)
            {
                throw new ArgumentNullException();
            }
            if (ContainsKey(key))
            {
                throw new ArgumentException();
            }

            //regular adding
            dict.Add(key, new LinkedListNode<Tuple<TValue, TKey>>(Tuple.Create(value, key)));
            list.AddLast(dict[key]);
        }

        public void Add(KeyValuePair<TKey, TValue> item)
        {
            Add(item.Key, item.Value);
        }

        public bool TryAdd(TKey key, TValue value)
        {
            if (key == null || ContainsKey(key))
            {
                return false;
            }

            Add(key, value);
            return true;
        }

        public void Clear()
        {
            dict.Clear();
            list.Clear();
        }

        public bool Contains(KeyValuePair<TKey, TValue> item)
        {
            if (!ContainsKey(item.Key))
            {
                return false;
            }

            return (dict[item.Key].Value.Item1.Equals(item.Value));
        }

        public bool ContainsKey(TKey key)
        {
            return dict.ContainsKey(key);
        }

        public void CopyTo(KeyValuePair<TKey, TValue>[] array, int arrayIndex)
        {
            int inc = arrayIndex;
            for (LinkedListNode<Tuple<TValue, TKey>> node = list.First; node != null; node = node.Next)
            {
                array[inc] = new KeyValuePair<TKey, TValue>(node.Value.Item2, node.Value.Item1);
                inc++;
            }
        }

        public bool Remove(TKey key)
        {
            if (!ContainsKey(key))
            {
                return false;
            }
            
            list.Remove(dict[key]);
            return dict.Remove(key);
        }

        public bool Remove(KeyValuePair<TKey, TValue> item)
        { //hz
            throw new NotImplementedException();
        }

        public bool TryGetValue(TKey key, out TValue value)
        {
            if (!ContainsKey(key))
            {
                value = default(TValue);
                return false;
            }

            value = dict[key].Value.Item1;
            return true;
        }

        public IEnumerator<KeyValuePair<TKey, TValue>> GetEnumerator()
        {
            return new LinkedDictionaryEnumerator<TKey, TValue>(list);
        }

        IEnumerator IEnumerable.GetEnumerator()
        {
            return (IEnumerator) GetEnumerator();
        }
    }

    internal class LinkedDictionaryEnumerator<TKey, TValue> : IEnumerator<KeyValuePair<TKey, TValue>>
    {
        LinkedListNode<Tuple<TValue, TKey>> currentNode = null;
        LinkedList<Tuple<TValue, TKey>> list;

        internal LinkedDictionaryEnumerator(LinkedList<Tuple<TValue, TKey>> list)
        {
            this.list = list;
        }

        KeyValuePair<TKey, TValue> IEnumerator<KeyValuePair<TKey, TValue>>.Current
        {
            get
            {
                if (currentNode == null)
                {
                    throw new InvalidOperationException();
                }
                return new KeyValuePair<TKey, TValue>(currentNode.Value.Item2, currentNode.Value.Item1);
            }
        }

        object IEnumerator.Current => throw new NotImplementedException();

        void IDisposable.Dispose() { } //dunno

        bool IEnumerator.MoveNext()
        {
            if (currentNode != null)
            {
                currentNode = currentNode.Next;
            }
            return (currentNode != null);
        }

        void IEnumerator.Reset()
        {
            currentNode = list.First;
        }
    }
}