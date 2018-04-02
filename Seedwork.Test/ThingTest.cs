using System;
using Xunit;

namespace Seedwork.Test
{
    public class ThingTest
    {
        [Fact]
        public void Test1()
        {
            Assert.Equal("42", new Thing().Get(19, 23));
        }
    }
}
